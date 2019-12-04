package edu.boisestate.elgamal;

import java.math.BigInteger;

public class ElGamalMessage implements  java.io.Serializable{
    private BigInteger ephimeralKey;
    private BigInteger encryptedMessage;

    public ElGamalMessage(BigInteger ephimeralKey, BigInteger encryptedMessage) {
        this.ephimeralKey = ephimeralKey;
        this.encryptedMessage = encryptedMessage;
    }

    public BigInteger getEphimeralKey() {
        return ephimeralKey;
    }

    public void setEphimeralKey(BigInteger ephimeralKey) {
        this.ephimeralKey = ephimeralKey;
    }

    public BigInteger getEncryptedMessage() {
        //System.out.println("returning == " + encryptedMessage);
        return encryptedMessage;
    }

    public void setEncryptedMessage(BigInteger encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }
}
