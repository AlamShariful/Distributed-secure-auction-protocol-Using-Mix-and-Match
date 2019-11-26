package GreaterThanFunction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import PET.CheckPET;
import Tables.TableRow;
import edu.boisestate.elgamal.ElGamal;
import edu.boisestate.elgamal.ElGamalMessage;
import edu.boisestate.elgamal.ElGamalPrivateKey;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class GreaterThanFunction
{
    private TableRow[] tableRow;
    private ElGamalMessage encOfOne, encOfNegOne, encOfZeroAlt;
    private ElGamalMessage previousSign;
    private CheckPET checkpet;

    private FileWriter fileWriter;

    public GreaterThanFunction(ElGamalMessage mEncOfOne, ElGamalMessage mEncOfNegOne, ElGamalMessage mEncOfZeroAlt, ElGamalMessage mPreviousSign) throws IOException
    {
        tableRow = new TableRow[12];

        for(int i=0;i<12;i++)
        {
            tableRow[i] = new TableRow();
        }

        encOfOne = mEncOfOne;
        encOfNegOne = mEncOfNegOne;
        encOfZeroAlt = mEncOfZeroAlt;

        previousSign = mPreviousSign;

        checkpet = new CheckPET();

        String path = System.getProperty("user.dir").toString() + "\\samplefile1.txt";
        fileWriter = new FileWriter(path);
    }

    public void generateFullGreaterThanTable()
    {
        int a = 0;
        int b = 1;
        int aCounter = 0;

        for(int i = 0; i < 12; i++)
        {
            //System.out.println("(i,j)=" + a + "," + b);
            if(tableRow[i] == null)
            {
                System.out.println("table object null!!");
                return;
            }
            //set A
            if(a == 0)      tableRow[i].setAi(encOfZeroAlt);
            else if(a == 1) tableRow[i].setAi(encOfOne);

            //set B
            if(b == 0)      tableRow[i].setBi(encOfZeroAlt);
            else if(b == 1) tableRow[i].setBi(encOfOne);

            //set sign, this is the previous sign
            if(a > b)       tableRow[i].setSign(encOfOne);  //one represents that the first one is greater
            else if(a == b) tableRow[i].setSign(encOfZeroAlt);  //zero represents that they are equal
            else if(a < b)  tableRow[i].setSign(encOfNegOne);  //negative one represents that the second one is greater

            //set outA
            if(previousSign.getEncryptedMessage().equals(encOfOne.getEncryptedMessage()) && previousSign.getEphimeralKey().equals(encOfOne.getEphimeralKey()))         tableRow[i].setOutA(encOfOne);
            else if(previousSign.getEncryptedMessage().equals(encOfNegOne.getEncryptedMessage()) && previousSign.getEphimeralKey().equals(encOfNegOne.getEphimeralKey())) tableRow[i].setOutA(encOfNegOne);
            else if(previousSign.getEncryptedMessage().equals(encOfZeroAlt.getEncryptedMessage()) && previousSign.getEphimeralKey().equals(encOfZeroAlt.getEphimeralKey()))
            {
                if(a > b)       tableRow[i].setOutA(encOfOne);
                else if(a == b) tableRow[i].setOutA(encOfZeroAlt);
                else if(a < b)  tableRow[i].setOutA(encOfNegOne);
            }

            //set outB
            if(tableRow[i].getOutA().getEncryptedMessage().equals(encOfOne.getEncryptedMessage()) && tableRow[i].getOutA().getEphimeralKey().equals(encOfOne.getEphimeralKey())) tableRow[i].setOutB(encOfNegOne);
            else if(tableRow[i].getOutA().getEncryptedMessage().equals(encOfNegOne.getEncryptedMessage()) && tableRow[i].getOutA().getEphimeralKey().equals(encOfNegOne.getEphimeralKey())) tableRow[i].setOutB(encOfOne);
            else tableRow[i].setOutB(encOfZeroAlt);

            //update a
            aCounter++;
            if(aCounter == 2)
            {
                aCounter = 0;
                if(a == 0) a = 1;
                else       a = 0;
            }
            //update b
            if(b == 1) b = 0;
            else       b = 1;
        }
    }
    public void PrintTable()
    {
        System.out.println("A              B           sign         outA          outB");
        for(int i = 0; i < 12; i++)
        {
            System.out.println(tableRow[i].getAi().getEncryptedMessage() + "         " + tableRow[i].getBi().getEncryptedMessage() + "         " + tableRow[i].getSign().getEncryptedMessage()
                               + "         " + tableRow[i].getOutA().getEncryptedMessage() + "          " + tableRow[i].getOutB().getEncryptedMessage());
        }
    }
    public void ShuffleTable()
    {
        int rand_int1, rand_int2;
        TableRow temp;

        for(int i=0;i<10;i++)
        {
            rand_int1 = ThreadLocalRandom.current().nextInt();
            rand_int2 = ThreadLocalRandom.current().nextInt();
            if(rand_int1 < 0) rand_int1 = rand_int1 * (-1);
            if(rand_int2 < 0) rand_int2 = rand_int2 * (-1);
            rand_int1 = rand_int1 % 12;
            rand_int2 = rand_int2 % 12;
            //System.out.println("rand ints = " + rand_int1 + " , " + rand_int2);

            temp = tableRow[rand_int1];
            tableRow[rand_int1] = tableRow[rand_int2];
            tableRow[rand_int2] = temp;
        }
    }
    public boolean CheckGreater(ElGamalMessage [] encNum1, ElGamalMessage [] encNum2, ElGamalPrivateKey privateKey) throws IOException
    {
        ElGamalMessage previousState = encOfZeroAlt;    //by default we assume that the numbers are equal
        usingFileWriter("initial previous state == " + previousState + "\n");
        //System.out.println("initial previous state == " + previousState);

        for(int bitIndex = 0; bitIndex < 1024; bitIndex++)
        {
            usingFileWriter("checking bit no" + bitIndex + "\n");
            //System.out.println("checking bit no" + bitIndex);
            for(int tableIndex = 0; tableIndex < 12; tableIndex++)
            {
                /*if((encNum1[bitIndex].getEncryptedMessage().equals(tableRow[tableIndex].getAi().getEncryptedMessage()) && encNum1[bitIndex].getEphimeralKey().equals(tableRow[tableIndex].getAi().getEphimeralKey()))
                   && (encNum2[bitIndex].getEncryptedMessage().equals(tableRow[tableIndex].getAi().getEncryptedMessage()) && encNum2[bitIndex].getEphimeralKey().equals(tableRow[tableIndex].getAi().getEphimeralKey()))
                   && (previousState.getEncryptedMessage().equals(tableRow[tableIndex].getAi().getEncryptedMessage()) && previousState.getEphimeralKey().equals(tableRow[tableIndex].getAi().getEphimeralKey())))
                {
                    previousState = tableRow[tableIndex].getOutA();
                    System.out.println("previous state == " + previousState);
                }*/
                usingFileWriter("checking with table bit Ai = " + ElGamal.decryptMessage(tableRow[tableIndex].getAi(), privateKey));
                usingFileWriter("matching with bit = " + ElGamal.decryptMessage(encNum1[bitIndex], privateKey));

                if(isBitMessageEqual(encNum1[bitIndex], tableRow[tableIndex].getAi(), privateKey))
                {

                    usingFileWriter("first col match" + "\n");
                    //System.out.println("first col match");
                    if(isBitMessageEqual(encNum2[bitIndex], tableRow[tableIndex].getBi(), privateKey))
                    {
                        usingFileWriter("second col match" + "\n");
                        //System.out.println("second col match");
                        if(isBitMessageEqual(previousState, tableRow[tableIndex].getSign(), privateKey))
                        {
                            previousState = tableRow[tableIndex].getOutA();
                            usingFileWriter("\n\n\n");
                            break;
                            //System.out.println("previous state == " + previousState);
                        }
                    }
                }
            }
        }

        if(previousState.getEncryptedMessage().equals(encOfOne.getEncryptedMessage()) && previousState.getEphimeralKey().equals(encOfOne.getEphimeralKey()))
        {
            return  true;
        }
        return false;
    }
    public boolean isBitMessageEqual(ElGamalMessage m1, ElGamalMessage m2, ElGamalPrivateKey privateKey) throws IOException
    {
        boolean result = checkpet.checkEqualityOfTwoMessage(m1, m2, privateKey);

        if(result)
        {
            usingFileWriter("pet returns true" + "\n");
        }
        else
        {
            usingFileWriter("pet returns false" + "\n");
        }
        return result;
    }
    public void usingFileWriter(String fileContent) throws IOException
    {
        fileWriter.write(fileContent);
        //fileWriter.close();
    }

}