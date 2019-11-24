package ElgamalTest;

import GreaterThanFunction.GreaterThanFunction;
import edu.boisestate.elgamal.ElGamal;
import edu.boisestate.elgamal.ElGamalMessage;
import edu.boisestate.elgamal.ElGamalPrivateKey;
import edu.boisestate.elgamal.ElGamalPublicKey;

import java.math.BigInteger;

public class TestGreaterThanTable {
    public static void main(String[] args)
    {

        ElGamalPrivateKey privateKey = ElGamal.generateKeyPair(12);
        System.out.println("Test Private key == " + privateKey.getPrivateKey());

        ElGamalPublicKey publicKey;
        publicKey = privateKey.getPublicKey();

        System.out.println("Test private key G == " + publicKey.getG());
        System.out.println("Test private key P == " + publicKey.getP());
        System.out.println("Test private key B == " + publicKey.getB());

        //new let's test GreaterThanFunction
        BigInteger one = BigInteger.ONE;
        BigInteger negOne = one.negate();
        BigInteger zeroAlt = BigInteger.valueOf(50);    //just for test, assuming that we represent 0 with 50
        System.out.println("onr = " + one + " and negOne = " + negOne);

        ElGamalMessage e;
        BigInteger encOne, encNegOne, encZeroAlt;
        //System.out.println("starting encryption:");

        e = ElGamal.encryptMessage(publicKey, one);
        encOne = e.getEncryptedMessage();
        //System.out.println("done1");

        e = ElGamal.encryptMessage(publicKey, zeroAlt);
        encZeroAlt = e.getEncryptedMessage();
        //System.out.println("done2");

        e = ElGamal.encryptMessage(publicKey, negOne);
        System.out.println("test");
        encNegOne = e.getEncryptedMessage();
        //System.out.println("done3");

        System.out.println("Encrypted values:\n" + encOne + "\n" + encNegOne + "\n" + encZeroAlt);
        System.out.println("starting table generation test:");
        GreaterThanFunction greaterThanFunction = new GreaterThanFunction(encOne, encNegOne, encZeroAlt, encZeroAlt);   //sign is equal
        greaterThanFunction.generateFullGreaterThanTable();
        greaterThanFunction.PrintTable();
        greaterThanFunction.ShuffleTable();
        greaterThanFunction.PrintTable();
    }
}
