package GreaterThanFunction;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

import Tables.TableRow;
import edu.boisestate.elgamal.ElGamalMessage;

public class GreaterThanFunction
{
    private TableRow[] tableRow;
    private ElGamalMessage encOfOne, encOfNegOne, encOfZeroAlt;
    private ElGamalMessage previousSign;

    public GreaterThanFunction(ElGamalMessage mEncOfOne, ElGamalMessage mEncOfNegOne, ElGamalMessage mEncOfZeroAlt, ElGamalMessage mPreviousSign)
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
            System.out.println(tableRow[i].getAi() + "         " + tableRow[i].getBi() + "         " + tableRow[i].getSign()
                               + "         " + tableRow[i].getOutA() + "          " + tableRow[i].getOutB());
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

}