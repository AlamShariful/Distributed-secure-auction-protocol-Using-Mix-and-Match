package ElgamalTest;

import GreaterThanFunction.GreaterThanFunction;
import PET.CheckPET;
import edu.boisestate.elgamal.*;

import java.io.IOException;
import java.math.BigInteger;

public class TestGreaterThanTable {
    public static void main(String[] args) throws IOException
    {

        int bits = 20;
        ElGamalPrivateKey privateKey = ElGamal.generateKeyPair(bits);
        //System.out.println("Test Private key == " + privateKey.getPrivateKey());

        ElGamalPublicKey publicKey;
        publicKey = privateKey.getPublicKey();

        System.out.println("Test private key G == " + publicKey.getG());
        System.out.println("Test private key P == " + publicKey.getP());
        System.out.println("Test private key B == " + publicKey.getB());

        //new let's test GreaterThanFunction
        //BigInteger one = BigInteger.ONE;
        BigInteger one = BigInteger.valueOf(1);
        BigInteger negOne = ElGamal.GetNegOneAlternative();    //just for test, assuming that we represent -1 with 2
        BigInteger zeroAlt = ElGamal.GetZeroAlternative();    //just for test, assuming that we represent 0 with 50
        //System.out.println("onr = " + one + " and negOne = " + negOne);

        ElGamalMessage encOne, encNegOne, encZeroAlt;
        //BigInteger encOne, encNegOne, encZeroAlt;
        //System.out.println("starting encryption:");

        encOne = ElGamal.encryptMessage(publicKey, one);
        //System.out.println("enc1 = " + encOne.getEncryptedMessage() + ", " + encOne.getEphimeralKey());
        //System.out.println("done1");

        encZeroAlt = ElGamal.encryptMessage(publicKey, zeroAlt);
        //System.out.println("enc0alt = " + encZeroAlt.getEncryptedMessage() + ", " + encZeroAlt.getEphimeralKey());
        //System.out.println("done2");

        encNegOne = ElGamal.encryptMessage(publicKey, negOne);
        //System.out.println("encNeg1 = " + encNegOne.getEncryptedMessage() + ", " + encNegOne.getEphimeralKey());
        //System.out.println("done3");

        //System.out.println("starting table generation test:");
        GreaterThanFunction greaterThanFunction = new GreaterThanFunction(encOne, encNegOne, encZeroAlt);   //sign is equal
        greaterThanFunction.generateFullGreaterThanTable();
        greaterThanFunction.PrintTable();
        greaterThanFunction.PrintDecryptedTable(privateKey);
        greaterThanFunction.ReShuffleAndReEncryptTable(publicKey);
        System.out.println("after reshuffle and reencryption:");
        greaterThanFunction.PrintDecryptedTable(privateKey);
        //greaterThanFunction.ShuffleTable();
        //greaterThanFunction.PrintTable();


        //so table genration is done, now let's check if it works on cipher texts
        //getting two cipher text
        BigInteger num1 = BigInteger.valueOf(22);
        BigInteger num2 = BigInteger.valueOf(15);

        BitbyBitEncryptionTable binary = new BitbyBitEncryptionTable();
        String num1S = binary.binaryTostring(num1);
        String num2S = binary.binaryTostring(num2);

        //System.out.println("first value in binary: " + num1S);
        //System.out.println("Second value in binary: " + num2S);

        ElGamalMessage [] encNum1= binary.splitstringAndencryption(num1S, publicKey);
        ElGamalMessage [] encNum2= binary.splitstringAndencryption(num2S, publicKey);

        //I here have the bitwise encryption of two plain texts: 10, and 15. now let's see if we can use this
        //along with my greater than table to figure out which one is bigger
        System.out.println("Checking the greater than function for the plain texts == " + num1 + ", " + num2);
        boolean result = greaterThanFunction.CheckGreater(encNum1, encNum2, privateKey);
        System.out.println("result == " + result);

        //greaterThanFunction.PrintTable();
        //greaterThanFunction.ShuffleTable();
        //greaterThanFunction.PrintTable();
        /*//ElGamalMessage e1 = ElGamal.encryptMessage(publicKey, num1);
        //ElGamalMessage e2 = ElGamal.encryptMessage(publicKey, num2);

        //same enc test
        encOne = ElGamal.encryptMessage(publicKey, one);
        System.out.println("enc1 = " + encOne.getEncryptedMessage() + ", " + encOne.getEphimeralKey());

        encOne = ElGamal.encryptMessage(publicKey, one);
        System.out.println("enc1 = " + encOne.getEncryptedMessage() + ", " + encOne.getEphimeralKey());

        encOne = ElGamal.encryptMessage(publicKey, one);
        System.out.println("enc1 = " + encOne.getEncryptedMessage() + ", " + encOne.getEphimeralKey());

        encOne = ElGamal.encryptMessage(publicKey, one);
        System.out.println("enc1 = " + encOne.getEncryptedMessage() + ", " + encOne.getEphimeralKey());

        //PET finction test
        ElGamalMessage one1, one2;

        one1 = ElGamal.encryptMessage(publicKey, one);
        System.out.println("one1 = " + one1.getEncryptedMessage() + ", " + one1.getEphimeralKey());

        one2 = ElGamal.encryptMessage(publicKey, one);
        System.out.println("one2 = " + one2.getEncryptedMessage() + ", " + one2.getEphimeralKey());

        encNegOne = ElGamal.encryptMessage(publicKey, negOne);
        System.out.println("neg1 = " + encNegOne.getEncryptedMessage() + ", " + encNegOne.getEphimeralKey());

        //decryption test
        BigInteger decryptedMessage;
        decryptedMessage = ElGamal.decryptMessage(one1, privateKey);
        System.out.println("decrypted message == " + decryptedMessage);

        decryptedMessage = ElGamal.decryptMessage(encNegOne, privateKey);
        System.out.println("decrypted message == " + decryptedMessage);
        //end
        CheckPET checkpet = new CheckPET();

        boolean result = checkpet.checkEqualityOfTwoMessage(encNegOne, encNegOne, privateKey);
        System.out.println("result == "  + result);*/
    }
}
