package Tables;

import java.math.BigInteger;

public class TableRow
{
    private BigInteger Ai, Bi, sign, outA, outB;

    public TableRow()
    {
        Ai = Bi = sign = outA = outB = BigInteger.ZERO;
    }

    public void setAi(BigInteger nAi) { Ai = nAi; }
    public void setBi(BigInteger nBi) { Bi = nBi; }
    public void setSign(BigInteger nSign) { sign = nSign; }
    public void setOutA(BigInteger nOutA) { outA = nOutA; }
    public void setOutB(BigInteger nOutB) { outB = nOutB; }

    public BigInteger getAi(){ return Ai; }
    public BigInteger getBi(){ return Bi; }
    public BigInteger getSign(){ return sign; }
    public BigInteger getOutA(){ return outA; }
    public BigInteger getOutB(){ return outB; }
}