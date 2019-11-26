package GreaterThanFunction;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

import PET.CheckPET;
import Tables.TableRow;
import edu.boisestate.elgamal.ElGamal;
import edu.boisestate.elgamal.ElGamalMessage;
import edu.boisestate.elgamal.ElGamalPrivateKey;


public class GreaterThanFunction
{
    private TableRow[] tableRow;
    private ElGamalMessage encOfOne, encOfNegOne, encOfZeroAlt;
    private CheckPET checkpet;

    public GreaterThanFunction(ElGamalMessage mEncOfOne, ElGamalMessage mEncOfNegOne, ElGamalMessage mEncOfZeroAlt)
    {
        tableRow = new TableRow[12];

        for(int i=0;i<12;i++)
        {
            tableRow[i] = new TableRow();
        }

        encOfOne = mEncOfOne;
        encOfNegOne = mEncOfNegOne;
        encOfZeroAlt = mEncOfZeroAlt;

        checkpet = new CheckPET();
    }

    public void generateFullGreaterThanTable()
    {
        int a = 0;
        int b = 1;
        int aCounter = 0;
        int signCounter = 0;
        ElGamalMessage previousState = encOfNegOne;

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
            tableRow[i].setSign(previousState);

            //set outA
            if(previousState.getEncryptedMessage().equals(encOfOne.getEncryptedMessage()) && previousState.getEphimeralKey().equals(encOfOne.getEphimeralKey()))         tableRow[i].setOutA(encOfOne);
            else if(previousState.getEncryptedMessage().equals(encOfNegOne.getEncryptedMessage()) && previousState.getEphimeralKey().equals(encOfNegOne.getEphimeralKey())) tableRow[i].setOutA(encOfNegOne);
            else if(previousState.getEncryptedMessage().equals(encOfZeroAlt.getEncryptedMessage()) && previousState.getEphimeralKey().equals(encOfZeroAlt.getEphimeralKey()))
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

            //update sign
            signCounter++;
            if(signCounter>3)
            {
                signCounter = 0;

                if(previousState.getEncryptedMessage().equals(encOfNegOne.getEncryptedMessage()) && previousState.getEphimeralKey().equals(encOfNegOne.getEphimeralKey()))
                {
                    previousState = encOfZeroAlt;
                }
                else if(previousState.getEncryptedMessage().equals(encOfZeroAlt.getEncryptedMessage()) && previousState.getEphimeralKey().equals(encOfZeroAlt.getEphimeralKey()))
                {
                    previousState = encOfOne;
                }
            }
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
    public void PrintDecryptedTable(ElGamalPrivateKey privateKey)
    {
        System.out.println("A   B   sign   outA   outB");
        for(int i = 0; i < 12; i++)
        {
            System.out.println(ElGamal.decryptMessage(tableRow[i].getAi(), privateKey) + "   " + ElGamal.decryptMessage(tableRow[i].getBi(), privateKey) + "   " + ElGamal.decryptMessage(tableRow[i].getSign(), privateKey)
                    + "       " + ElGamal.decryptMessage(tableRow[i].getOutA(), privateKey) + "     " + ElGamal.decryptMessage(tableRow[i].getOutB(),privateKey));
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

            temp = tableRow[rand_int1];
            tableRow[rand_int1] = tableRow[rand_int2];
            tableRow[rand_int2] = temp;
        }
    }
    public boolean CheckGreater(ElGamalMessage [] encNum1, ElGamalMessage [] encNum2, ElGamalPrivateKey privateKey)
    {
        ElGamalMessage previousState = encOfZeroAlt;    //by default we assume that the numbers are equal
        //usingFileWriter("initial previous state == " + previousState + "\n");
        //System.out.println("initial previous state == " + previousState);

        for(int bitIndex = 0; bitIndex < 1024; bitIndex++)
        {
            for(int tableIndex = 0; tableIndex < 12; tableIndex++)
            {
                if(isBitMessageEqual(encNum1[bitIndex], tableRow[tableIndex].getAi(), privateKey))
                {
                    if(isBitMessageEqual(encNum2[bitIndex], tableRow[tableIndex].getBi(), privateKey))
                    {
                        if(isBitMessageEqual(previousState, tableRow[tableIndex].getSign(), privateKey))
                        {
                            previousState = tableRow[tableIndex].getOutA();
                            break;
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
    public boolean isBitMessageEqual(ElGamalMessage m1, ElGamalMessage m2, ElGamalPrivateKey privateKey)
    {
        return checkpet.checkEqualityOfTwoMessage(m1, m2, privateKey);
    }
}