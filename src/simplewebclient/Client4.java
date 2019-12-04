package simplewebclient;

import PET.CheckPET;
import edu.boisestate.elgamal.*;
import otherFunctions.ElGamalBitMessageConversion;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client4 extends Thread{
    private static final String hostName = "localhost";
    //private static final int PORT = 8089;

    private static final int PORT = 6589;

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



                /// split string bit by bit and Encrypt each bit
                ElGamalMessage[] getInputTable= binary.splitstringAndencryption(s, publicKey);

                //String representation of ciphertext
                ciphertext = ElGamalBitMessageConversion.ElgamalBitMessageToString(getInputTable);

                // Sending Cipher text to Server
                System.out.println("sending bits: " + ciphertext);
                out.writeUTF(ciphertext);
                out.flush();
                //out.close();
                System.out.println("Wating for Server Response");

                // wait for server response
                while (true){
                    try {
                        //Socket soc = Socket.accept();
                        //String winningBid= (String) dis.readUTF();
                        String winningBid = (String) objectInputStream.readObject();
                        System.out.println("Winning Bid: "+winningBid);

                        if(winningBid!=""){
                            out.close();
                            break;
                        }

                    }catch (EOFException e){
                        System.out.println(e);
                        break;
                    }
                }


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
