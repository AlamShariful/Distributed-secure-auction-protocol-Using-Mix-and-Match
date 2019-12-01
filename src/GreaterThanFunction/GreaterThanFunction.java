package GreaterThanFunction;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import PET.CheckPET;
import Tables.TableRow;
import edu.boisestate.elgamal.ElGamal;
import edu.boisestate.elgamal.ElGamalMessage;
import edu.boisestate.elgamal.ElGamalPrivateKey;
import edu.boisestate.elgamal.ElGamalPublicKey;


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
    public void PrintGroupDecryptedTable(List<ElGamalPrivateKey> privateKeys)
    {
        TableRow[] tempTableRow = new TableRow[12];
        for(int i=0;i<12;i++)
        {
            tempTableRow[i] = new TableRow();

            tempTableRow[i].setAi(tableRow[i].getAi());
            tempTableRow[i].setBi(tableRow[i].getBi());
            tempTableRow[i].setSign(tableRow[i].getSign());
            tempTableRow[i].setOutA(tableRow[i].getOutA());
            tempTableRow[i].setOutB(tableRow[i].getOutB());
        }

        for(int i=0;i<12;i++)
        {
            /*for(int j=0;j<privateKeys.size();j++)
            {
                tempTableRow[i].setAi(ElGamal.decryptGroupMessage(tempTableRow[i].getAi(), privateKeys.get(j)));
                tempTableRow[i].setBi(ElGamal.decryptGroupMessage(tempTableRow[i].getBi(), privateKeys.get(j)));
                tempTableRow[i].setSign(ElGamal.decryptGroupMessage(tempTableRow[i].getSign(), privateKeys.get(j)));
                tempTableRow[i].setOutA(ElGamal.decryptGroupMessage(tempTableRow[i].getOutA(), privateKeys.get(j)));
                tempTableRow[i].setOutB(ElGamal.decryptGroupMessage(tempTableRow[i].getOutB(), privateKeys.get(j)));
            }*/
            tempTableRow[i].setAi(ElGamal.decryptDistributedMessage(tempTableRow[i].getAi(), privateKeys));
            tempTableRow[i].setBi(ElGamal.decryptDistributedMessage(tempTableRow[i].getBi(), privateKeys));
            tempTableRow[i].setSign(ElGamal.decryptDistributedMessage(tempTableRow[i].getSign(), privateKeys));
            tempTableRow[i].setOutA(ElGamal.decryptDistributedMessage(tempTableRow[i].getOutA(), privateKeys));
            tempTableRow[i].setOutB(ElGamal.decryptDistributedMessage(tempTableRow[i].getOutB(), privateKeys));
        }
        int A=-2, B=-2, sign=-2, OutA=-2, OutB=-2;
        BigInteger decA, decB, decSign, decOutA, decOutB;
        System.out.println("A   B   sign   outA   outB");
        for(int i = 0; i < 12; i++)
        {

            decA = tempTableRow[i].getAi().getEncryptedMessage();
            if(decA.equals(BigInteger.valueOf(1))) A = 1;
            else if(decA.equals(BigInteger.valueOf(2))) A = -1;
            else if(decA.equals(BigInteger.valueOf(3))) A = 0;

            decB = tempTableRow[i].getBi().getEncryptedMessage();
            if(decB.equals(BigInteger.valueOf(1))) B = 1;
            else if(decB.equals(BigInteger.valueOf(2))) B = -1;
            else if(decB.equals(BigInteger.valueOf(3))) B = 0;

            decSign = tempTableRow[i].getSign().getEncryptedMessage();
            if(decSign.equals(BigInteger.valueOf(1))) sign = 1;
            else if(decSign.equals(BigInteger.valueOf(2))) sign = -1;
            else if(decSign.equals(BigInteger.valueOf(3))) sign = 0;

            decOutA = tempTableRow[i].getOutA().getEncryptedMessage();
            if(decOutA.equals(BigInteger.valueOf(1))) OutA = 1;
            else if(decOutA.equals(BigInteger.valueOf(2))) OutA = -1;
            else if(decOutA.equals(BigInteger.valueOf(3))) OutA = 0;

            decOutB = tempTableRow[i].getOutB().getEncryptedMessage();
            if(decOutB.equals(BigInteger.valueOf(1))) OutB = 1;
            else if(decOutB.equals(BigInteger.valueOf(2))) OutB = -1;
            else if(decOutB.equals(BigInteger.valueOf(3))) OutB = 0;


            System.out.println(A + "   " + B + "   " + sign + "       " + OutA + "     " + OutB);
        }
    }

    public void PrintDecryptedTable(ElGamalPrivateKey privateKey)
    {
        int A=-2, B=-2, sign=-2, OutA=-2, OutB=-2;
        BigInteger decA, decB, decSign, decOutA, decOutB;
        System.out.println("A   B   sign   outA   outB");
        for(int i = 0; i < 12; i++)
        {

            decA = ElGamal.decryptMessage(tableRow[i].getAi(), privateKey);
            if(decA.equals(BigInteger.valueOf(1))) A = 1;
            else if(decA.equals(BigInteger.valueOf(2))) A = -1;
            else if(decA.equals(BigInteger.valueOf(3))) A = 0;

            decB = ElGamal.decryptMessage(tableRow[i].getBi(), privateKey);
            if(decB.equals(BigInteger.valueOf(1))) B = 1;
            else if(decB.equals(BigInteger.valueOf(2))) B = -1;
            else if(decB.equals(BigInteger.valueOf(3))) B = 0;

            decSign = ElGamal.decryptMessage(tableRow[i].getSign(), privateKey);
            if(decSign.equals(BigInteger.valueOf(1))) sign = 1;
            else if(decSign.equals(BigInteger.valueOf(2))) sign = -1;
            else if(decSign.equals(BigInteger.valueOf(3))) sign = 0;

            decOutA = ElGamal.decryptMessage(tableRow[i].getOutA(), privateKey);
            if(decOutA.equals(BigInteger.valueOf(1))) OutA = 1;
            else if(decOutA.equals(BigInteger.valueOf(2))) OutA = -1;
            else if(decOutA.equals(BigInteger.valueOf(3))) OutA = 0;

            decOutB = ElGamal.decryptMessage(tableRow[i].getOutB(), privateKey);
            if(decOutB.equals(BigInteger.valueOf(1))) OutB = 1;
            else if(decOutB.equals(BigInteger.valueOf(2))) OutB = -1;
            else if(decOutB.equals(BigInteger.valueOf(3))) OutB = 0;


            System.out.println(A + "   " + B + "   " + sign + "       " + OutA + "     " + OutB);
        }
    }

    public void ReShuffleAndReEncryptTable(ElGamalPublicKey publicKey)
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

            //re-shuffle
            temp = tableRow[rand_int1];
            tableRow[rand_int1] = tableRow[rand_int2];
            tableRow[rand_int2] = temp;

            //re-encrypt
            tableRow[rand_int1].setAi(ElGamal.reEncryptMessage(tableRow[rand_int1].getAi(), publicKey));    //re-encrypt Ai col
            tableRow[rand_int1].setBi(ElGamal.reEncryptMessage(tableRow[rand_int1].getBi(), publicKey));    //re-encrypt Bi col
            tableRow[rand_int1].setSign(ElGamal.reEncryptMessage(tableRow[rand_int1].getSign(), publicKey));    //re-encrypt sign col
            tableRow[rand_int1].setOutA(ElGamal.reEncryptMessage(tableRow[rand_int1].getOutA(), publicKey));    //re-encrypt OutA col
            tableRow[rand_int1].setOutB(ElGamal.reEncryptMessage(tableRow[rand_int1].getOutB(), publicKey));    //re-encrypt OutB col

            tableRow[rand_int2].setAi(ElGamal.reEncryptMessage(tableRow[rand_int2].getAi(), publicKey));    //re-encrypt Ai col
            tableRow[rand_int2].setBi(ElGamal.reEncryptMessage(tableRow[rand_int2].getBi(), publicKey));    //re-encrypt Bi col
            tableRow[rand_int2].setSign(ElGamal.reEncryptMessage(tableRow[rand_int2].getSign(), publicKey));    //re-encrypt sign col
            tableRow[rand_int2].setOutA(ElGamal.reEncryptMessage(tableRow[rand_int2].getOutA(), publicKey));    //re-encrypt OutA col
            tableRow[rand_int2].setOutB(ElGamal.reEncryptMessage(tableRow[rand_int2].getOutB(), publicKey));    //re-encrypt OutB col
        }
    }
    public boolean CheckGreater(ElGamalMessage [] encNum1, ElGamalMessage [] encNum2, ElGamalPrivateKey privateKey)
    {
        ElGamalMessage previousState = encOfZeroAlt;    //by default we assume that the numbers are equal
        //usingFileWriter("initial previous state == " + previousState + "\n");
        System.out.println("initial previous state == " + previousState);

        for(int bitIndex = 0; bitIndex < 1024; bitIndex++)
        {
            System.out.println("checking for bit == " + bitIndex);
            System.out.print("Ai = " + ElGamal.decryptMessage(encNum1[bitIndex], privateKey) + ", Bi = " + ElGamal.decryptMessage(encNum2[bitIndex], privateKey) + " ");
            for(int tableIndex = 0; tableIndex < 12; tableIndex++)
            {
                if(isBitMessageEqual(encNum1[bitIndex], tableRow[tableIndex].getAi(), privateKey))
                {
                    if(isBitMessageEqual(encNum2[bitIndex], tableRow[tableIndex].getBi(), privateKey))
                    {
                        if(isBitMessageEqual(previousState, tableRow[tableIndex].getSign(), privateKey))
                        {
                            previousState = tableRow[tableIndex].getOutA();
                            System.out.println("Updated sign: " + ElGamal.decryptMessage(previousState, privateKey));
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("finally the updated state: " + ElGamal.decryptMessage(previousState, privateKey));
        BigInteger result = ElGamal.decryptMessage(previousState, privateKey);
        if(result.equals(BigInteger.valueOf(1)))
        {
            return  true;
        }
        return false;
    }
    public boolean CheckDistributedGreater(ElGamalMessage [] encNum1, ElGamalMessage [] encNum2, List<ElGamalPrivateKey> privateKeys)
    {
        ElGamalMessage previousState = encOfZeroAlt;    //by default we assume that the numbers are equal
        //usingFileWriter("initial previous state == " + previousState + "\n");
        //System.out.println("initial previous state == " + ElGamal.decryptDistributedMessage(previousState, privateKeys).getEncryptedMessage());

        for(int bitIndex = 0; bitIndex < 1024; bitIndex++)
        {
            //System.out.println("checking for bit == " + bitIndex);
            //System.out.print("Ai = " + ElGamal.decryptDistributedMessage(encNum1[bitIndex], privateKeys).getEncryptedMessage() + ", Bi = " + ElGamal.decryptDistributedMessage(encNum2[bitIndex], privateKeys).getEncryptedMessage() + " " + ElGamal.decryptDistributedMessage(previousState, privateKeys).getEncryptedMessage()+ " \n");
            for(int tableIndex = 0; tableIndex < 12; tableIndex++)
            {
                if(isDistributedBitMessageEqual(encNum1[bitIndex], tableRow[tableIndex].getAi(), privateKeys))
                {
                    if(isDistributedBitMessageEqual(encNum2[bitIndex], tableRow[tableIndex].getBi(), privateKeys))
                    {
                        if(isDistributedBitMessageEqual(previousState, tableRow[tableIndex].getSign(), privateKeys))
                        {
                            previousState = tableRow[tableIndex].getOutA();
                            //System.out.println("Updated sign: " + ElGamal.decryptMessage(previousState, privateKeys));


                            //BigInteger temp;
                            //temp = ElGamal.decryptDistributedMessage(previousState, privateKeys).getEncryptedMessage();

                            //System.out.println("Updated sign: " + temp);


                            break;
                        }
                    }
                }
            }
        }
        //System.out.println("finally the updated state: " + ElGamal.decryptMessage(previousState, privateKey));
        BigInteger result;

        result = ElGamal.decryptDistributedMessage(previousState, privateKeys).getEncryptedMessage();

        if(result.equals(BigInteger.valueOf(1)))
        {
            return  true;
        }
        return false;
    }
    public boolean isBitMessageEqual(ElGamalMessage m1, ElGamalMessage m2, ElGamalPrivateKey privateKey)
    {
        return checkpet.checkEqualityOfTwoMessage(m1, m2, privateKey);
    }
    public boolean isDistributedBitMessageEqual(ElGamalMessage m1, ElGamalMessage m2, List<ElGamalPrivateKey> privateKeys)
    {
        return checkpet.checkDistributedEqualityOfTwoMessage(m1, m2, privateKeys);
    }
}