package simplewebclient;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

import edu.boisestate.elgamal.*;


public class SimpleWebClient {
    private static final String hostName = "localhost";
    private static final int PORT = 8089;
    public static void main(String[] args) throws IOException {
        try (
                Socket serverSocket = new Socket(hostName, PORT);

                // Read user input from console
                BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in));

                //Send user command to Server Via socket
                DataOutputStream out = new DataOutputStream(serverSocket.getOutputStream());


                // Read server response from socket
                //BufferedReader in =new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        ) {
            //String userInput;


            // Read Biginteger input from client
            BigInteger userInput;
            Scanner sc = new Scanner(System.in);
            String ciphertext="";

            //if ((userInput = stdIn.readLine()) != null) {
            if ((userInput = sc.nextBigInteger()) != null) {

                System.out.println("Original message:"+userInput);

                // convert UserInput to Binary
                BitbyBitEncryptionTable binary = new BitbyBitEncryptionTable();
                String s = binary.binaryTostring(userInput);
                System.out.println("Binary form:"+s);

                // split string bit by bit and Encrypt each bit
                BigInteger [] getInputTable= binary.splitstringAndencryption(s);

                for (int i=0; i<getInputTable.length;i++){
                    System.out.println("Table Output"+getInputTable[i]);
                    //ciphertext=ciphertext.concat(getInputTable[i].toString());
                    if (ciphertext == ""){
                        ciphertext=getInputTable[i].toString();
                    }else{
                        ciphertext=ciphertext +"."+getInputTable[i].toString();
                    }

                    System.out.println("Ciphertext"+ciphertext);
                }

                // Sending Cipher text to Server
                out.writeUTF(ciphertext);
                out.flush();
                out.close();




                /*
                //second try(working) to pass ElGamal stuff to server
                ElGamalPrivateKey privateKey = ElGamal.generateKeyPair(10);
                System.out.println("Test Private key == " + privateKey.getPrivateKey());

                ElGamalPublicKey publicKey;
                publicKey = privateKey.getPublicKey();

                System.out.println("Test private key G == " + publicKey.getG());
                System.out.println("Test private key P == " + publicKey.getP());
                System.out.println("Test private key B == " + publicKey.getB());



                ElGamalMessage eMessage= ElGamal.encryptMessage(publicKey,userInput);
                System.out.println("Elgamal message:"+eMessage);

                System.out.println("passing keys and message to server: ");
                String totalMessage = privateKey.getPrivateKey() + " " + publicKey.getG() + " " + publicKey.getP() + " " + publicKey.getB()
                                                                 + " " + userInput + " " + eMessage;
                System.out.println(totalMessage);
                out.writeUTF(String.valueOf(totalMessage));

                */

                //second try ends
                /* Trying to use Elgamal, Testing failed
                
                // take user input and encrypt with elgamal
                ElGamalPrivateKey privateKey = ElGamal.generateKeyPair(1024);
                System.out.println("Elgamal Private Key:"+privateKey);

                ElGamalPublicKey publicKey = new ElGamalPublicKey();
                publicKey.getP();
                ElGamalMessage eMessage= ElGamal.encryptMessage(publicKey,userInput);
                System.out.println("Elgamal message:"+eMessage);


                */





            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +  hostName);
            System.exit(1);
        }
    }
}
