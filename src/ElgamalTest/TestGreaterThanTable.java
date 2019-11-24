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

        ElGamalMessage encOne, encNegOne, encZeroAlt;
        //BigInteger encOne, encNegOne, encZeroAlt;
        //System.out.println("starting encryption:");

        encOne = ElGamal.encryptMessage(publicKey, one);
        System.out.println("enc1 = " + encOne.getEncryptedMessage() + ", " + encOne.getEphimeralKey());
        //System.out.println("done1");

        encZeroAlt = ElGamal.encryptMessage(publicKey, zeroAlt);
        System.out.println("enc0alt = " + encZeroAlt.getEncryptedMessage() + ", " + encZeroAlt.getEphimeralKey());
        //System.out.println("done2");

        encNegOne = ElGamal.encryptMessage(publicKey, negOne);
        System.out.println("encNeg1 = " + encNegOne.getEncryptedMessage() + ", " + encNegOne.getEphimeralKey());
        //System.out.println("done3");

        System.out.println("starting table generation test:");
        GreaterThanFunction greaterThanFunction = new GreaterThanFunction(encOne, encNegOne, encZeroAlt, encZeroAlt);   //sign is equal
        greaterThanFunction.generateFullGreaterThanTable();
        greaterThanFunction.PrintTable();
        greaterThanFunction.ShuffleTable();
        greaterThanFunction.PrintTable();
    }
}
