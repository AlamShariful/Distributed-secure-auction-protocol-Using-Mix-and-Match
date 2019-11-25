package edu.boisestate.elgamal;

import java.math.BigInteger;

public class ElGamalPublicKey {
    private BigInteger p;
    private BigInteger g;
    private BigInteger B;
    private static BigInteger zeroAlternative = BigInteger.valueOf(51);

    public BigInteger GetZeroAlternative()
    {
        return zeroAlternative;
    }
    public BigInteger getP() {
        return p;
    }

    public void setP(BigInteger p) {
        if (p != null && ElGamal.isSafePrime(p)) {
            this.p = p;
        } else {
            System.err.println("prime is not safe");
        }
    }

    public BigInteger getG() {
        return g;
    }

    public void setG(BigInteger g) {
        if (g != null && p != null && ElGamal.isPrimitive(g, p)) {
            this.g = g;
        } else {
            System.err.println("g is is not primitive");
        }
    }

    public BigInteger getB() {
        return B;
    }

    public void setB(BigInteger b) {
        if (b != null && p != null && b.compareTo(p.subtract(BigInteger.valueOf(2))) <= 0 && b.compareTo(BigInteger.valueOf(2)) >= 0) {
            this.B = b;
        } else {
            System.err.println("b must be between 2 and p-2");
        }
    }
}
