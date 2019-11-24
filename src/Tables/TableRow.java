package Tables;

import edu.boisestate.elgamal.ElGamalMessage;

import java.math.BigInteger;

public class TableRow
{
    private ElGamalMessage Ai, Bi, sign, outA, outB;

    public TableRow()
    {
        Ai = Bi = sign = outA = outB = null;
    }

    public void setAi(ElGamalMessage nAi) { Ai = nAi; }
    public void setBi(ElGamalMessage nBi) { Bi = nBi; }
    public void setSign(ElGamalMessage nSign) { sign = nSign; }
    public void setOutA(ElGamalMessage nOutA) { outA = nOutA; }
    public void setOutB(ElGamalMessage nOutB) { outB = nOutB; }

    public ElGamalMessage getAi(){ return Ai; }
    public ElGamalMessage getBi(){ return Bi; }
    public ElGamalMessage getSign(){ return sign; }
    public ElGamalMessage getOutA(){ return outA; }
    public ElGamalMessage getOutB(){ return outB; }
}