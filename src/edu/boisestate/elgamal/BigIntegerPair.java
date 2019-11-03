package edu.boisestate.elgamal;

import java.math.BigInteger;

public class BigIntegerPair {

    private BigInteger s;
    private BigInteger t;
    private BigInteger d;

    public BigIntegerPair() {
        this(BigInteger.ZERO, BigInteger.ZERO);
    }

    public BigIntegerPair(BigInteger s, BigInteger t) {
        this.s = s;
        this.t = t;
    }

    public BigInteger getS() {
        return s;
    }

    public void setS(BigInteger s) {
        this.s = s;
    }

    public BigInteger getT() {
        return t;
    }

    public void setT(BigInteger t) {
        this.t = t;
    }

    public BigInteger getD() {
        return d;
    }

    public void setD(BigInteger d) {
        this.d = d;
    }
}
