package simplewebserver;


import Multiple_Servers.Elgamal_interface;
import Test_RMI_Multiple_Server.Search;
import Test_RMI_Multiple_Server.SearchQuery;
import edu.boisestate.elgamal.ElGamal;
import edu.boisestate.elgamal.ElGamalMessage;
import edu.boisestate.elgamal.ElGamalPrivateKey;
import edu.boisestate.elgamal.ElGamalPublicKey;
import otherFunctions.ElGamalBitMessageConversion;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import static java.nio.file.StandardOpenOption.APPEND;
import java.nio.file.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.*;
//import com.google.gson.*;

public class SimpleWebServer{

    /* Run the HTTP server on this TCP port. */
    private static final int PORT = 8089;
    //private static BigInteger server1_publicKey,server2_publicKey, server3_publicKey,server1_privateKey,server2_privateKey,server3_privateKey;
    private static ElGamalPublicKey server1_publicKey,server2_publicKey, server3_publicKey,commonPublicKey;
    private static ElGamalPrivateKey server1_privateKey,server2_privateKey,server3_privateKey;
    public static String msg_Client1,msg_Client2,msg_Client3,msg_Client4;

    public static int i=0;

    private int bits = 20;
    private BigInteger p = ElGamal.generateSafePrime(bits);
    private BigInteger g = ElGamal.generateRandomPrimitive(p);
    /* The socket used to process incoming connections from web clients */
    private static ServerSocket dServerSocket;

    //List for Holding Grouppublic key
    List<ElGamalPublicKey> groupPublicKey = new ArrayList<ElGamalPublicKey>();

    public SimpleWebServer () throws Exception {
        dServerSocket = new ServerSocket (PORT);
        getting_publicKey_For_This_server();
        getting_publicKey_From_server2();
        getting_publicKey_From_server3();

        //get Group Public Key
        System.out.println("generating group public key: " + groupPublicKey.size());
        commonPublicKey=ElGamal.getGroupPublicKey(groupPublicKey);
        System.out.println("group public key generated: " + groupPublicKey.size());
    }

    public void run() throws Exception {

        // We are Condidering 4 Client, therefore this loop should Run 4 time (Connect 4 client)

        //while (true) {
        while (i<4) {
            try{
                /* wait for a connection from a client */
                Socket s = dServerSocket.accept();
                System.out.println("Client: "+Integer.sum(i,1)+" Connected");

                // Send output stream Object Via the socket. To client
                OutputStream outputStream = s.getOutputStream();
                ObjectOutputStream obj =new ObjectOutputStream(outputStream);
                obj.writeObject(commonPublicKey);

                // read Encrypted messege from User
                DataInputStream dis=new DataInputStream(s.getInputStream());
                String  str=(String)dis.readUTF();


                //Assign Received Strings to Different Variables
                if(i==0){
                    // Client 1 is sending Msg
                    msg_Client1=str;
                    System.out.println("Client 1 received Encrypted Message in string = " + msg_Client1);
                    ElGamalMessage [] bitMsg_client1 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client1);
                    System.out.println("equality test == " + msg_Client1.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client1)));
                }
                if(i==1){
                    // Client 2 is sending Msg
                    msg_Client2=str;
                    System.out.println("Client 2 received Encrypted Message in string = " + msg_Client2);
                    ElGamalMessage [] bitMsg_client2 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client2);
                    System.out.println("equality test == " + msg_Client2.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client2)));
                }
                if(i==2){
                    // Client 3 is sending Msg
                    msg_Client3=str;
                    System.out.println("Client 3 received Encrypted Message in string = " + msg_Client3);
                    ElGamalMessage [] bitMsg_client3 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client3);
                    System.out.println("equality test == " + msg_Client3.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client3)));
                }
                if(i==3){
                    // Client 4 is sending Msg
                    msg_Client4=str;
                    System.out.println("Client 4 received Encrypted Message in string = " + msg_Client4);
                    ElGamalMessage [] bitMsg_client4 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client4);
                    System.out.println("equality test == " + msg_Client4.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client4)));
                }


                //Count Connected Client
                i++;



            }catch (Exception e){System.out.println(e);}
        }
        System.out.println("4 messages collected. Messages are: \n" + msg_Client1 + "\n" + msg_Client2 + "\n"+ msg_Client3 + "\n"+ msg_Client4 + "\n");
    }

    // calling remote server to additional support
    public void getting_publicKey_For_This_server(){
        BigInteger result=null;
        ElGamalPrivateKey privateKey= new ElGamalPrivateKey();


        //call Elgamal package to get private key
        privateKey = ElGamal.generateKeyPair(bits, p, g);

        //private key
        //server1_privateKey=privateKey.getPrivateKey();
        server1_privateKey=privateKey;

        //get public key
        server1_publicKey= privateKey.getPublicKey();

        // add this public key to groupPublicKey list
        groupPublicKey.add(server1_publicKey);


        System.out.println("Server 1 Private Key: "+server1_privateKey.getPrivateKey());
        System.out.println("Server 1 Public key: "+server1_publicKey.getP()+","+server1_publicKey.getG()+","+server1_publicKey.getB());

    }

    public void getting_publicKey_From_server2(){
        ElGamalPrivateKey answer;
        String value="get_privateKey";
        try
        {
            // lookup method to find reference of remote object
            Elgamal_interface privateKey1 =
                    (Elgamal_interface) Naming.lookup("rmi://localhost:1900"+
                            "/privateKey");

            answer = privateKey1.generate_privatekey(value, bits, p, g);

            // server 2 key pair
            server2_publicKey=answer.getPublicKey();
            server2_privateKey=answer;

            // add this public key to groupPublicKey list
            groupPublicKey.add(server2_publicKey);

            //System.out.println("From Elgamal_Interface__Implementation_call Private Key: "+answer.getPrivateKey());
            //System.out.println("From Elgamal_Interface__Implementation_call Public Key: "+answer.getPublicKey().getG());

            System.out.println("Server 2 Private Key: "+server2_privateKey.getPrivateKey());
            System.out.println("Server 2 Public key: "+server2_publicKey.getP()+","+server2_publicKey.getG()+","+server2_publicKey.getB());
        }
        catch(Exception ae)
        {
            System.out.println(ae);
        }
    }

    public void getting_publicKey_From_server3(){
        ElGamalPrivateKey answer;
        String value="get_privateKey";
        try
        {
            // lookup method to find reference of remote object
            Elgamal_interface privateKey1 =
                    (Elgamal_interface) Naming.lookup("rmi://localhost:2000"+
                            "/privateKey");
            answer = privateKey1.generate_privatekey(value, bits, p, g);

            // server 3 keypair
            server3_publicKey=answer.getPublicKey();
            server3_privateKey=answer;

            // add this public key to groupPublicKey list
            groupPublicKey.add(server3_publicKey);

            //System.out.println("From Elgamal_Interface__Implementation_call Private Key: "+answer.getPrivateKey());
            //System.out.println("From Elgamal_Interface__Implementation_call Public Key: "+answer.getPublicKey().getG());

            System.out.println("Server 3 Private Key: "+server3_privateKey.getPrivateKey());
            System.out.println("Server 3 Public key: "+server3_publicKey.getP()+","+server3_publicKey.getG()+","+server3_publicKey.getB());
        }
        catch(Exception ae)
        {
            System.out.println(ae);
        }
    }



    /* This method is called when the program is run from the command line. */
    public static void main (String argv[]) throws Exception {
        /* Create a SimpleWebServer object, and run it */
        SimpleWebServer sws = new SimpleWebServer();

        // run the coordinator server (this server)
        sws.run();



    }
}
