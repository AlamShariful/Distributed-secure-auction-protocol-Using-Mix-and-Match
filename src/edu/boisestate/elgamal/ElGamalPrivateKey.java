package edu.boisestate.elgamal;

import java.math.BigInteger;

public class ElGamalPrivateKey implements  java.io.Serializable{

    private BigInteger privateKey;
    private ElGamalPublicKey publicKey;

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    public ElGamalPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(ElGamalPublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
