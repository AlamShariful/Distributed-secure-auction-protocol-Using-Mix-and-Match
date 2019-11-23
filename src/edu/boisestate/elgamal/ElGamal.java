package edu.boisestate.elgamal;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ElGamal {

    private final static int PRIME_UNCERTAINTY = 1000000;
    private final static SecureRandom rnd = new SecureRandom();

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
        System.err.println(g);
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
            System.out.println("coming here");
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
        BigInteger maskingKeyInverse = extendedEuclidAlgorithm(maskingKey, p).getT();
        BigInteger message = encryptedMessage.getEncryptedMessage().multiply(maskingKeyInverse).mod(p);

        return message;
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
