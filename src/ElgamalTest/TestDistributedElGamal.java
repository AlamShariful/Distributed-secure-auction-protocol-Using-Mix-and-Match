package ElgamalTest;

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

        BitbyBitEncryptionTable binary = new BitbyBitEncryptionTable();

        BigInteger m = BigInteger.valueOf(16);
        System.out.println("Encrypting: " + m);
        ElGamalMessage encMessage = ElGamal.encryptMessage(globalPublicKey, m);
        System.out.println("Encrypted message: " + encMessage.getEncryptedMessage() + ", " + encMessage.getEphimeralKey());

        System.out.println("Decrypting the grouped encrypted message:");
        ElGamalMessage mPrime = ElGamal.decryptGroupMessage(encMessage, privateKey1);
        System.out.println("after decrypting once: " + mPrime.getEncryptedMessage() + ", " + mPrime.getEphimeralKey());

        ElGamalMessage message = ElGamal.decryptGroupMessage(mPrime, privateKey2);
        System.out.println("after decrypting twice: " + message.getEncryptedMessage() + ", " + message.getEphimeralKey());

        System.out.println("Full Decrypted message: " + message.getEncryptedMessage());
    }
}
