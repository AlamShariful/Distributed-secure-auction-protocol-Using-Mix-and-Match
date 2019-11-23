package GreaterThanFunction;

import java.math.BigInteger;
import Tables.TableRow;

public class GreaterThanFunction
{
    private TableRow[] tableRow;
    private BigInteger encOfOne, encOfNegOne, encOfZeroAlt;
    private BigInteger previousSign;

    public GreaterThanFunction(BigInteger mEncOfOne, BigInteger mEncOfNegOne, BigInteger mEncOfZeroAlt, BigInteger mPreviousSign)
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
            if(previousSign.equals(encOfOne))         tableRow[i].setOutA(encOfOne);
            else if(previousSign.equals(encOfNegOne)) tableRow[i].setOutA(encOfNegOne);
            else if(previousSign.equals(encOfZeroAlt))
            {
                if(a > b)       tableRow[i].setOutA(encOfOne);
                else if(a == b) tableRow[i].setOutA(encOfZeroAlt);
                else if(a < b)  tableRow[i].setOutA(encOfNegOne);
            }

            //set outB
            if(tableRow[i].getOutA().equals(encOfOne)) tableRow[i].setOutB(encOfNegOne);
            else if(tableRow[i].getOutA().equals(encOfNegOne)) tableRow[i].setOutB(encOfOne);
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
}