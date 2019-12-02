package simplewebclient;

import PET.CheckPET;
import edu.boisestate.elgamal.*;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client2 extends Thread{
    private static final String hostName = "localhost";
    private static final int PORT = 8089;

    public static void main(String[] args) throws IOException {
        try (
                Socket serverSocket = new Socket(hostName, PORT);

                /*
                Try to Read Input Stream From Server Via Object Stream.
                Read Commonpublic Key from Server
                * */
                InputStream inputStream = serverSocket.getInputStream();

                // create a DataInputStream so we can read data from it.
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                //END

                // Read user input from console
                BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in));

                //Send user command to Server Via socket
                DataOutputStream out = new DataOutputStream(serverSocket.getOutputStream());
        ) {

            //Read commom Public Key from server
            ElGamalPublicKey publicKey = (ElGamalPublicKey) objectInputStream.readObject();
            System.out.println("Elgamal Object, Public Key: " + publicKey.getP() + "," + publicKey.getG() + "," + publicKey.getB());


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

                /*
                Following Section is taken care of inside get Common Public Key Function
                */

                /*

                // Elgamal Key Generation
                ElGamalPrivateKey privateKey = ElGamal.generateKeyPair(12);
                System.out.println("Test Private key == " + privateKey.getPrivateKey());
                // Moved inside Multiple_servers_interface



                ElGamalPublicKey publicKey;
                publicKey = privateKey.getPublicKey();

                System.out.println("Test private key G == " + publicKey.getG());
                System.out.println("Test private key P == " + publicKey.getP());
                System.out.println("Test private key B == " + publicKey.getB());





                END */


                // split string bit by bit and Encrypt each bit
                //ElGamalMessage [] getInputTable= binary.splitstringAndencryption(s, publicKey);
                ElGamalMessage[] getInputTable= binary.splitstringAndencryption(s, publicKey);

                for (int i=0; i<getInputTable.length;i++){
                    System.out.println("Table Output"+getInputTable[i].getEncryptedMessage() + " , " + getInputTable[i].getEphimeralKey());
                    //ciphertext=ciphertext.concat(getInputTable[i].toString());
                    if (ciphertext == ""){
                        ciphertext=getInputTable[i].getEncryptedMessage().toString() + "," + getInputTable[i].getEphimeralKey().toString();
                    }else{
                        ciphertext=ciphertext +"."+getInputTable[i].getEncryptedMessage().toString() + "," + getInputTable[i].getEphimeralKey().toString();
                    }

                    System.out.println("Ciphertext"+ciphertext);
                }

                // Sending Cipher text to Server
                out.writeUTF(ciphertext);
                out.flush();
                out.close();



                // Everything in the bottom should go the server
                //Current Implementation is Showing False because, I'm generating a new private Key
                // Using BElow line, Remove it

                // Remove Following Line
                ElGamalPrivateKey privateKey = ElGamal.generateKeyPair(12);

                ElGamalMessage m1= ElGamal.encryptMessage(publicKey,BigInteger.valueOf(10));
                ElGamalMessage m2= ElGamal.encryptMessage(publicKey,BigInteger.valueOf(10));

                CheckPET check=new CheckPET();
                Boolean result =check.checkEqualityOfTwoMessage(m1,m2,privateKey);
                System.out.println(result);



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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
