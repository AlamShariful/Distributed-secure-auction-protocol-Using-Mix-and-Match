package ElgamalTest;

import GreaterThanFunction.GreaterThanFunction;
import edu.boisestate.elgamal.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TestDistributedElGamal
{
    public static void main(String[] args)
    {
        System.out.println("TestDistributedElGamal");

        //two dummy servers and their public, private keys
        int bits = 20;

        BigInteger p = ElGamal.generateSafePrime(bits);
        BigInteger g = ElGamal.generateRandomPrimitive(p);
        System.out.println("P = " + p);
        System.out.println("G = " + g);
        ElGamalPrivateKey privateKey1 = ElGamal.generateKeyPair(bits, p, g);
        ElGamalPublicKey publicKey1 = privateKey1.getPublicKey();

        ElGamalPrivateKey privateKey2 = ElGamal.generateKeyPair(bits, p, g);
        ElGamalPublicKey publicKey2 = privateKey2.getPublicKey();

        System.out.println(publicKey1.getP());
        System.out.println(publicKey2.getP());
        //generate group public key
        List<ElGamalPublicKey> list=new ArrayList<ElGamalPublicKey>();
        list.add(publicKey1);
        list.add(publicKey2);
        ElGamalPublicKey globalPublicKey = ElGamal.getGroupPublicKey(list);
        System.out.println("group public key generated");


        System.out.println("Encrypt sample message in a simple way");

        BigInteger m = BigInteger.valueOf(16);
        System.out.println("Encrypting: " + m);
        ElGamalMessage encMessage = ElGamal.encryptMessage(globalPublicKey, m);
        System.out.println("Encrypted message: " + encMessage.getEncryptedMessage() + ", " + encMessage.getEphimeralKey());

        List<ElGamalPrivateKey> list1=new ArrayList<ElGamalPrivateKey>();
        list1.add(privateKey1);
        list1.add(privateKey2);

        System.out.println("first key: private: " + privateKey1.getPrivateKey() + ", P: " + privateKey1.getPublicKey().getP() + ", G: " + privateKey1.getPublicKey().getG() + ", B: " + privateKey1.getPublicKey().getB());
        System.out.println("second key: private: " + privateKey2.getPrivateKey() + ", P: " + privateKey2.getPublicKey().getP() + ", G: " + privateKey2.getPublicKey().getG() + ", B: " + privateKey2.getPublicKey().getB());

        BigInteger message = ElGamal.decryptDistributedMessage(encMessage, list1).getEncryptedMessage();

        System.out.println("Full Decrypted message: " + message);


        //Greater than table generation test
        BigInteger one = BigInteger.valueOf(1);
        BigInteger negOne = ElGamal.GetNegOneAlternative();    //just for test, assuming that we represent -1 with 2
        BigInteger zeroAlt = ElGamal.GetZeroAlternative();    //just for test, assuming that we represent 0 with 50
        ElGamalMessage encOne, encNegOne, encZeroAlt;

        encOne = ElGamal.encryptMessage(globalPublicKey, one);
        encZeroAlt = ElGamal.encryptMessage(globalPublicKey, zeroAlt);
        encNegOne = ElGamal.encryptMessage(globalPublicKey, negOne);

        GreaterThanFunction greaterThanFunction = new GreaterThanFunction(encOne, encNegOne, encZeroAlt);   //sign is equal
        greaterThanFunction.generateFullGreaterThanTable();
        greaterThanFunction.PrintTable();
        greaterThanFunction.ReShuffleAndReEncryptTable(globalPublicKey);
        greaterThanFunction.PrintTable();


        greaterThanFunction.PrintGroupDecryptedTable(list1);


        //Testing greater than table and PET
        System.out.println("testing PET");
        BigInteger num1 = BigInteger.valueOf(3);
        BigInteger num2 = BigInteger.valueOf(1);

        BitbyBitEncryptionTable binary = new BitbyBitEncryptionTable();
        String num1S = binary.binaryTostring(num1);
        String num2S = binary.binaryTostring(num2);

        //System.out.println("first value in binary: " + num1S);
        //System.out.println("Second value in binary: " + num2S);

        ElGamalMessage [] encNum1= binary.splitstringAndencryption(num1S, globalPublicKey);
        ElGamalMessage [] encNum2= binary.splitstringAndencryption(num2S, globalPublicKey);

        System.out.println("Checking the greater than function for the plain texts == " + num1 + ", " + num2);
        boolean result = greaterThanFunction.CheckDistributedGreater(encNum1, encNum2, list1);
        System.out.println("result == " + result);
    }
}
