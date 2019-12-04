package edu.boisestate.elgamal;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

public class ElGamal {

    private final static int PRIME_UNCERTAINTY = 1000000;
    private final static SecureRandom rnd = new SecureRandom();

    private static BigInteger one = BigInteger.ONE;
    private static BigInteger zeroAlternative = BigInteger.valueOf(3);
    private static BigInteger negOneAlternative = BigInteger.valueOf(2);

    public static BigInteger GetOne(){ return one;}
    public static BigInteger GetZeroAlternative() { return zeroAlternative; }
    public static BigInteger GetNegOneAlternative() { return negOneAlternative; }

    /**
     * Generates a random ElGamal Key Pair of a specific bit count
     * @param bits number of bits in numbers generated
     * @return random ElGamal key pair
     */
    public static ElGamalPrivateKey generateKeyPair(int bits) {
        BigInteger p = generateSafePrime(bits);
        BigInteger g = generateRandomPrimitive(p);
        BigInteger b = new BigInteger(bits, rnd);
        b = b.mod(p.subtract(BigInteger.valueOf(2))).add(BigInteger.valueOf(2)); // random value between 2 and p-2
        assert g != null;
        //System.err.println(g);
        BigInteger B = g.modPow(b, p);
        ElGamalPublicKey publicKey = new ElGamalPublicKey();
        publicKey.setP(p);
        publicKey.setG(g);
        publicKey.setB(B);
        ElGamalPrivateKey privateKey = new ElGamalPrivateKey();
        privateKey.setPrivateKey(b);
        privateKey.setPublicKey(publicKey);

        return privateKey;
    }
    //Key pair generation for groups requires that the p and g is common
    public static ElGamalPrivateKey generateKeyPair(int bits, BigInteger p, BigInteger g) {
        //BigInteger p = generateSafePrime(bits);
        //BigInteger g = generateRandomPrimitive(p);
        BigInteger b = new BigInteger(bits, rnd);
        b = b.mod(p.subtract(BigInteger.valueOf(2))).add(BigInteger.valueOf(2)); // random value between 2 and p-2
        assert g != null;
        //System.err.println(g);
        BigInteger B = g.modPow(b, p);
        ElGamalPublicKey publicKey = new ElGamalPublicKey();
        publicKey.setP(p);
        publicKey.setG(g);
        publicKey.setB(B);
        ElGamalPrivateKey privateKey = new ElGamalPrivateKey();
        privateKey.setPrivateKey(b);
        privateKey.setPublicKey(publicKey);

        return privateKey;
    }

    /**
     * Generates an ElGamal message with a public key
     * @param publicKey
     * @param message
     * @return
     */
    public static ElGamalMessage encryptMessage(ElGamalPublicKey publicKey, BigInteger message) {
        if (message.compareTo(publicKey.getP()) >= 0 || message.compareTo(BigInteger.ZERO) == 0) {
            return null;
        }

        BigInteger p = publicKey.getP();
        BigInteger g = publicKey.getG();
        BigInteger B = publicKey.getB();

        // Between 2 and p-2
        BigInteger ephimeralExponent = new BigInteger(p.bitCount(), rnd);
        ephimeralExponent = ephimeralExponent.mod(p.subtract(BigInteger.valueOf(2))).add(BigInteger.valueOf(2));

        BigInteger ephimeralKey = g.modPow(ephimeralExponent, p);
        BigInteger maskingKey = B.modPow(ephimeralExponent, p);

        BigInteger encryptedMessage = message.multiply(maskingKey).mod(p);

        ElGamalMessage elGamalMessage = new ElGamalMessage(ephimeralKey, encryptedMessage);

        return elGamalMessage;
    }

    /**
     * Decrypts an ElGamal message with a private key
     * @param encryptedMessage encrypted message
     * @param privateKey private key, with its public key inside
     * @return decrypted message
     */
    public static BigInteger decryptMessage(ElGamalMessage encryptedMessage, ElGamalPrivateKey privateKey) {
        ElGamalPublicKey publicKey = privateKey.getPublicKey();
        BigInteger p = publicKey.getP();

        BigInteger maskingKey = encryptedMessage.getEphimeralKey().modPow(privateKey.getPrivateKey(), p);
        BigInteger maskingKeyInverse = extendedEuclidAlgorithm(p, maskingKey).getT();
        maskingKeyInverse = maskingKeyInverse.mod(p);
        BigInteger message = encryptedMessage.getEncryptedMessage().multiply(maskingKeyInverse).mod(p);

        return message;
    }
    public static ElGamalMessage [] partialBitbyBitDecryption(ElGamalMessage [] encryptedMessage, ElGamalPrivateKey privateKey)
    {
        ElGamalMessage [] result = encryptedMessage;

        for(int i=0;i<result.length;i++)
        {
            result[i] = decryptGroupMessage(result[i], privateKey);
        }

        return result;
    }
    public static ElGamalMessage decryptDistributedMessage(ElGamalMessage encryptedMessage, List<ElGamalPrivateKey> privateKeys) {
        ElGamalMessage result = encryptedMessage;

        for(int i=0;i<privateKeys.size();i++)
        {
            result = decryptGroupMessage(result, privateKeys.get(i));
        }
        return result;
    }
    public static String decryptDistributedMessageBitByBit(ElGamalMessage [] encryptedMessage, List<ElGamalPrivateKey> privateKeys)
    {
        String result = "";
        ElGamalMessage temp;

        for(int i=0;i<encryptedMessage.length;i++)
        {
            temp = decryptDistributedMessage(encryptedMessage[i], privateKeys);

            result = result + temp.getEncryptedMessage().toString();
        }

        return result;
    }
    public static String getStringToBitbyBitDecryption(ElGamalMessage [] decryptedMessage)
    {
        String result = "";
        ElGamalMessage temp;

        for(int i=0;i<decryptedMessage.length;i++)
        {
            result = result + decryptedMessage[i].getEncryptedMessage().toString();
        }

        return result;
    }


    public static ElGamalMessage decryptGroupMessage(ElGamalMessage encryptedMessage, ElGamalPrivateKey privateKey) {
        ElGamalPublicKey publicKey = privateKey.getPublicKey();
        BigInteger p = publicKey.getP();

        BigInteger maskingKey = encryptedMessage.getEphimeralKey().modPow(privateKey.getPrivateKey(), p);
        BigInteger maskingKeyInverse = extendedEuclidAlgorithm(p, maskingKey).getT();
        maskingKeyInverse = maskingKeyInverse.mod(p);
        BigInteger message = encryptedMessage.getEncryptedMessage().multiply(maskingKeyInverse).mod(p);

        ElGamalMessage mPrime = new ElGamalMessage(encryptedMessage.getEphimeralKey(),message);

        return mPrime;
    }



    public static ElGamalMessage reEncryptMessage(ElGamalMessage encryptedMessage, ElGamalPublicKey publicKey)
    {
        BigInteger one = BigInteger.valueOf(1);
        ElGamalMessage encOne = encryptMessage(publicKey, one);

        BigInteger reEncryptedAlpha = encryptedMessage.getEncryptedMessage().multiply(encOne.getEncryptedMessage());
        BigInteger reEncryptedBeta = encryptedMessage.getEphimeralKey().multiply(encOne.getEphimeralKey());

        reEncryptedAlpha = reEncryptedAlpha.mod(publicKey.getP());
        reEncryptedBeta = reEncryptedBeta.mod(publicKey.getP());

        ElGamalMessage reEncryptedMessage = new ElGamalMessage(reEncryptedBeta, reEncryptedAlpha);

        return reEncryptedMessage;
    }
    public static ElGamalPublicKey getGroupPublicKey(List<ElGamalPublicKey> publicKeys)
    {
        //System.out.println("size == " + publicKeys.size());
        //System.out.println("Param: " + publicKeys.get(0).getB());
        //System.out.println("Param: " + publicKeys.get(1).getB());

        BigInteger groupB = BigInteger.ONE;
        for(int i=0;i<publicKeys.size();i++)
        {
            groupB = groupB.multiply(publicKeys.get(i).getB());
            //System.out.println("gB = " + groupB);
        }
        groupB = groupB.mod(publicKeys.get(0).getP());
        //System.out.println("Finally gB = " + groupB);
        BigInteger groupP = publicKeys.get(0).getP();
        BigInteger groupG = publicKeys.get(0).getG();
        //System.out.println("gP = " + groupP);
        ElGamalPublicKey groupPublicKey = new ElGamalPublicKey();
        groupPublicKey.setP(groupP);
        groupPublicKey.setG(groupG);
        groupPublicKey.setB(groupB);

        return groupPublicKey;
    }

    public static BigIntegerPair extendedEuclidAlgorithm(BigInteger a, BigInteger b) {
        BigIntegerPair pair = new BigIntegerPair();

        extendedEuclid(a, b, pair);

        return pair;
    }

    /**
     * Calculates the extended euclid of two numbers, where public attributes s and t are set accordingly.
     * @param a
     * @param b
     */
    private static void extendedEuclid(BigInteger a, BigInteger b, BigIntegerPair pair) {
        if (b.equals(BigInteger.ZERO)) {
            pair.setS(BigInteger.ONE);
            pair.setT(BigInteger.ZERO);
            pair.setD(a);
            return;
        }

        extendedEuclid(b, a.mod(b), pair);
        BigInteger x1 = pair.getT();
        BigInteger y1 = pair.getS().subtract(a.divide(b).multiply(pair.getT()));
        pair.setS(x1);
        pair.setT(y1);
    }

    public static BigInteger generateSafePrime(int bits) {
        BigInteger p;

        do {
            p = new BigInteger(bits, rnd);
        } while (!isSafePrime(p));

        return p;
    }

    /**
     * Generates a random primitive from prime
     * @param p a safe prime
     * @return returns a generator of prime p iff p is a safe prime, otherwise returns null
     */
    public static BigInteger generateRandomPrimitive(BigInteger p) {
        if(!isSafePrime(p)) {
            return null;
        }

        BigInteger g = null;
        int bits = p.bitCount();

        while (g == null) {
            g = new BigInteger(bits, rnd);
            g = g.mod(p);

            if (!isPrimitive(g, p)) {
                g = null;
            }
        }

        return g;
    }

    /**
     * Check if prime is a safe prime p, such that p = 2*q +1 , where q is also prime
     * @param p prime candidate
     * @return is probably a safe a prime
     */
    public static boolean isSafePrime(BigInteger p) {
        // Check if p is safe prime
        if (!p.isProbablePrime(PRIME_UNCERTAINTY)) {
            return false;
        }

        if (!p.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)).isProbablePrime(PRIME_UNCERTAINTY)) {
            return false;
        }

        return true;
    }

    /**
     * Checks whether g is primitive (generator) of Zp*, where p is a safe prime
     * @param g primitive candidate in [2, p-2]
     * @param p safe prime for Zp*
     * @return returns if it is a primitive of Zp* iff p is a safe prime, otherwise returns false
     */
    public static boolean isPrimitive(BigInteger g, BigInteger p) {
        if (!isSafePrime(p)) {
            return false;
        }

        if (g.compareTo(BigInteger.ZERO) == 0) {
            return false;
        }

        // Check that g^2 != 1 mod p
        if (g.modPow(new BigInteger("2"), p).compareTo(BigInteger.ONE) == 0) {
            return false;
        }

        // Check that g^((p-1)/2) != 1 mod p
        BigInteger tmp = g.modPow(g, p.subtract(BigInteger.ONE).divide(new BigInteger("2")));
        if (tmp.compareTo(BigInteger.ONE) == 0) {
            return false;
        }

        return true;
    }
}
