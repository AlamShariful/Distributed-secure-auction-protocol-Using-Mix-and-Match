package edu.boisestate.elgamal;

import Tables.SenderEncryptionTableRow;

import java.math.BigInteger;

public class BitbyBitEncryptionTable {


    public static String binaryTostring(BigInteger userInput){
        return userInput.toString(2);
    }


    public static ElGamalMessage[] splitstringAndencryption(String s, ElGamalPublicKey publicKey){

        //padding
        int siz = s.length();
        int paddingSize = 1024 - siz;

        String oneZero = "0";
        String allZeroes = "";
        for(int i=0;i<paddingSize;i++)
        {
            allZeroes = allZeroes + oneZero;
        }
        System.out.println("required zeroes == " + allZeroes.length());

        //adding padding
        s = allZeroes.concat(s);
        System.out.println("number string after adding padding: " + s);

        System.out.println("Test private key G == " + publicKey.getG());
        System.out.println("Test private key P == " + publicKey.getP());
        System.out.println("Test private key B == " + publicKey.getB());

        SenderEncryptionTableRow senderEncryptionTableRow = new SenderEncryptionTableRow();

        BigInteger one = BigInteger.ONE;
        BigInteger zeroAlt = publicKey.GetZeroAlternative();


        // split binary string into bit by bit
        for (int i=0; i < s.length(); i++){
            char current=s.charAt(i);

            // check for current bit, either 0/1
            if (current=='1'){
                // Current bit is 1. Send encryption of something
                System.out.println("Inside if:"+ s.charAt(i));


                // Apply Elgamal Encryption
                ElGamalMessage eMessage= ElGamal.encryptMessage(publicKey,one);
                senderEncryptionTableRow.setInput(eMessage, i);
                System.out.println("Elgamal message:"+eMessage.getEncryptedMessage());

            }else{
                // Current Bit is 0. Send encryption of something
                System.out.println("Inside else:"+ s.charAt(i));


                // Apply Elgamal Encryption
                ElGamalMessage eMessage= ElGamal.encryptMessage(publicKey,zeroAlt);
                senderEncryptionTableRow.setInput(eMessage, i);
                System.out.println("Elgamal message:"+eMessage.getEncryptedMessage());
            }



        }
        return senderEncryptionTableRow.getInput();


    }







}
