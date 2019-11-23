package edu.boisestate.elgamal;

import java.math.BigInteger;

public class BitbyBitEncryptionTable {


    public static String binaryTostring(BigInteger userInput){
        return userInput.toString(2);
    }


    public static void splitstringAndencryption(String s){




        ElGamalPrivateKey privateKey = ElGamal.generateKeyPair(10);
        System.out.println("Test Private key == " + privateKey.getPrivateKey());

        ElGamalPublicKey publicKey;
        publicKey = privateKey.getPublicKey();

        System.out.println("Test private key G == " + publicKey.getG());
        System.out.println("Test private key P == " + publicKey.getP());
        System.out.println("Test private key B == " + publicKey.getB());


        // split binary string into bit by bit
        for (int i=0; i < s.length(); i++){
            char current=s.charAt(i);

            // check for current bit, either 0/1
            if (current=='1'){
                // Current bit is 1. Send encryption of something
                System.out.println("Inside if:"+ s.charAt(i));

                // Convert Character into BigInteger for encryption, Change it accordingly
                BigInteger one = BigInteger.ONE;

                // Apply Elgamal Encryption
                ElGamalMessage eMessage= ElGamal.encryptMessage(publicKey,one);
                System.out.println("Elgamal message:"+eMessage);

            }else{
                // Current Bit is 0. Send encryption of something
                System.out.println("Inside else:"+ s.charAt(i));

                // Convert Character into BigInteger for encryption,Change it accordingly
                BigInteger zero = BigInteger.ZERO;

                // Apply Elgamal Encryption
                ElGamalMessage eMessage= ElGamal.encryptMessage(publicKey,zero);
                System.out.println("Elgamal message:"+eMessage);
            }



        }

    }







}
