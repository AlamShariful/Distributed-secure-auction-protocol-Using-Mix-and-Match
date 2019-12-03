package simplewebserver;


import GreaterThanFunction.GreaterThanFunction;
import Multiple_Servers.Elgamal_interface;
import Test_RMI_Multiple_Server.Search;
import Test_RMI_Multiple_Server.SearchQuery;
import edu.boisestate.elgamal.*;
import otherFunctions.BitResultHandler;
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
    public ElGamalMessage [] decrypt_msg_from_server_2,decrypt_msg_from_server_3;


    //Interface
    Elgamal_interface call_server2,call_server3;


    //List for Holding Grouppublic key
    List<ElGamalPublicKey> groupPublicKey = new ArrayList<ElGamalPublicKey>();

    public static int i=0;

    private int bits = 20;
    private BigInteger p = ElGamal.generateSafePrime(bits);
    private BigInteger g = ElGamal.generateRandomPrimitive(p);

    //all biddings
    private ElGamalMessage [][] allBiddings = new ElGamalMessage[4][];
    private int allBiddingCounter = 0;

    //greater than table
    private GreaterThanFunction greaterThanFunction;

    /* The socket used to process incoming connections from web clients */
    private static ServerSocket dServerSocket;


    public SimpleWebServer () throws Exception {
        dServerSocket = new ServerSocket (PORT);

        // calling remote servers
        calling_remote_Servers_interface();
        getting_publicKey_For_This_server();
        getting_publicKey_From_server2();
        getting_publicKey_From_server3();

        // test call to remote server 2/3,place it accordingly
        //send_Elgamal_msg_to_Server2_for_decryption();
        //send_Elgamal_msg_to_Server3_for_decryption();

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
                    allBiddings[allBiddingCounter] = new ElGamalMessage[bitMsg_client1.length];
                    allBiddings[allBiddingCounter] = bitMsg_client1;
                    allBiddingCounter++;
                    System.out.println("equality test == " + msg_Client1.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client1)));
                }
                if(i==1){
                    // Client 2 is sending Msg
                    msg_Client2=str;
                    System.out.println("Client 2 received Encrypted Message in string = " + msg_Client2);
                    ElGamalMessage [] bitMsg_client2 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client2);
                    allBiddings[allBiddingCounter] = new ElGamalMessage[bitMsg_client2.length];
                    allBiddings[allBiddingCounter] = bitMsg_client2;
                    allBiddingCounter++;
                    System.out.println("equality test == " + msg_Client2.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client2)));
                }
                if(i==2){
                    // Client 3 is sending Msg
                    msg_Client3=str;
                    System.out.println("Client 3 received Encrypted Message in string = " + msg_Client3);
                    ElGamalMessage [] bitMsg_client3 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client3);
                    allBiddings[allBiddingCounter] = new ElGamalMessage[bitMsg_client3.length];
                    allBiddings[allBiddingCounter] = bitMsg_client3;
                    allBiddingCounter++;
                    System.out.println("equality test == " + msg_Client3.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client3)));
                }
                if(i==3){
                    // Client 4 is sending Msg
                    msg_Client4=str;
                    System.out.println("Client 4 received Encrypted Message in string = " + msg_Client4);
                    ElGamalMessage [] bitMsg_client4 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client4);
                    allBiddings[allBiddingCounter] = new ElGamalMessage[bitMsg_client4.length];
                    allBiddings[allBiddingCounter] = bitMsg_client4;
                    allBiddingCounter++;
                    System.out.println("equality test == " + msg_Client4.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client4)));
                }


                //Count Connected Client
                i++;



            }catch (Exception e){System.out.println(e);}
        }
        System.out.println("4 messages collected. Messages are: \n" + msg_Client1 + "\n" + msg_Client2 + "\n"+ msg_Client3 + "\n"+ msg_Client4 + "\n");
        System.out.println("checking equality with global storage:");
        System.out.println(ElGamalBitMessageConversion.ElgamalBitMessageToString(allBiddings[0]).equals(msg_Client1) + ", " + ElGamalBitMessageConversion.ElgamalBitMessageToString(allBiddings[1]).equals(msg_Client2) + ", " + ElGamalBitMessageConversion.ElgamalBitMessageToString(allBiddings[2]).equals(msg_Client3) + ", " + ElGamalBitMessageConversion.ElgamalBitMessageToString(allBiddings[3]).equals(msg_Client4));

        //initialize greater than table
        initializeGreaterThanTable();
        //we take bit by bit of pair users
        ElGamalMessage [] result = allBiddings[0];
        for(int i=1;i<allBiddingCounter;i++)
        {
            result = findGreater(result, allBiddings[i]);
        }

        System.out.println("winner cipher text found!!, Decrypting bid: ");
        BigInteger winner = decryptWinningBid(result);

        System.out.println("winner == " + winner);

    }
    private void initializeGreaterThanTable()
    {
        //prepare greater than table
        BigInteger one = BigInteger.valueOf(1);
        BigInteger negOne = ElGamal.GetNegOneAlternative();    //just for test, assuming that we represent -1 with 2
        BigInteger zeroAlt = ElGamal.GetZeroAlternative();    //just for test, assuming that we represent 0 with 50

        ElGamalMessage encOne, encNegOne, encZeroAlt;
        encOne = ElGamal.encryptMessage(commonPublicKey, one);
        encZeroAlt = ElGamal.encryptMessage(commonPublicKey, zeroAlt);
        encNegOne = ElGamal.encryptMessage(commonPublicKey, negOne);

        greaterThanFunction = new GreaterThanFunction(encOne, encNegOne, encZeroAlt);   //sign is equal
        greaterThanFunction.generateFullGreaterThanTable();
        greaterThanFunction.PrintTable();
        greaterThanFunction.ReShuffleAndReEncryptTable(commonPublicKey);
        greaterThanFunction.PrintTable();
    }
    private ElGamalMessage [] findGreater(ElGamalMessage [] bid1, ElGamalMessage [] bid2)
    {
        //test with all private key for now
        List<ElGamalPrivateKey> allPrivateKeys=new ArrayList<ElGamalPrivateKey>();
        allPrivateKeys.add(server1_privateKey);
        allPrivateKeys.add(server2_privateKey);
        allPrivateKeys.add(server3_privateKey);
        //execute pairwuse comparison
        if(greaterThanFunction.CheckDistributedGreater(bid1, bid2, allPrivateKeys))
        {
            return bid1;
        }
        else if(greaterThanFunction.CheckDistributedGreater(bid2, bid1, allPrivateKeys))
        {
            return bid2;
        }
        else
        {
            return bid1;
        }
    }
    private BigInteger decryptWinningBid(ElGamalMessage [] result)
    {
        /*all together
        //test with all private key for now
        List<ElGamalPrivateKey> allPrivateKeys=new ArrayList<ElGamalPrivateKey>();
        allPrivateKeys.add(server1_privateKey);
        allPrivateKeys.add(server2_privateKey);
        allPrivateKeys.add(server3_privateKey);

        //decrypting
        String resultBid = ElGamal.decryptDistributedMessageBitByBit(result, allPrivateKeys);
        resultBid = BitResultHandler.normalizeBitString(resultBid);

        BigInteger decimalResult = BitResultHandler.BitStringToDecimalBigInteger(resultBid);

        return decimalResult;*/
        //separate server by server decryption
        BigInteger resultPlainText;
        ElGamalMessage [] decryptedMessage = result;
        String decryptedMessageString;
        //main server
        decryptedMessage = ElGamal.partialBitbyBitDecryption(result, server1_privateKey);

        //second server
        decryptedMessage = send_Elgamal_msg_to_Server2_for_decryption(ElGamalBitMessageConversion.ElgamalBitMessageToString(decryptedMessage));
        //decryptedMessage = ElGamalBitMessageConversion.StringToElgamalBitMessage(decryptedMessageString);
        //third server
        decryptedMessage = send_Elgamal_msg_to_Server3_for_decryption(ElGamalBitMessageConversion.ElgamalBitMessageToString(decryptedMessage));
        //decryptedMessage = ElGamalBitMessageConversion.StringToElgamalBitMessage(decryptedMessageString);

        String resultBid = ElGamal.getStringToBitbyBitDecryption(decryptedMessage);
        //System.out.println("decrypted message string == " + decryptedMessageString);
        //String resultBid;
        resultBid = BitResultHandler.normalizeBitString(resultBid);
        BigInteger decimalResult = BitResultHandler.BitStringToDecimalBigInteger(resultBid);

        return decimalResult;
    }

    // calling remote server to additional support
    public void calling_remote_Servers_interface(){
        try{
            // lookup method to find reference of remote object
            call_server2 = (Elgamal_interface) Naming.lookup("rmi://localhost:1900"+
                            "/privateKey");
            call_server3 = (Elgamal_interface) Naming.lookup("rmi://localhost:2000"+
                            "/privateKey");
        }catch (Exception e){
            System.out.println(e);
        }
    }

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
            answer = call_server2.generate_privatekey(value, bits, p, g);
            // server 2 key pair
            server2_publicKey=answer.getPublicKey();
            server2_privateKey=answer;

            // add this public key to groupPublicKey list
            groupPublicKey.add(server2_publicKey);

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
            answer = call_server3.generate_privatekey(value, bits, p, g);

            // server 3 keypair
            server3_publicKey=answer.getPublicKey();
            server3_privateKey=answer;

            // add this public key to groupPublicKey list
            groupPublicKey.add(server3_publicKey);

            System.out.println("Server 3 Private Key: "+server3_privateKey.getPrivateKey());
            System.out.println("Server 3 Public key: "+server3_publicKey.getP()+","+server3_publicKey.getG()+","+server3_publicKey.getB());
        }
        catch(Exception ae)
        {
            System.out.println(ae);
        }
    }

    // Written today on 12/2/2019
    public ElGamalMessage [] send_Elgamal_msg_to_Server2_for_decryption(String message){
        String drycpt="get_messege";

        // replace this value with the appropriate Elgamal_msg type
        //ElGamalMessage m1=null;
        try{
            //calling interface function "decrypt_messege" on server 2
            decrypt_msg_from_server_2= ElGamalBitMessageConversion.StringToElgamalBitMessage(call_server2.decrypt_messege(message,drycpt));
            System.out.println("Calling Server 2 from remote Decryption: "+decrypt_msg_from_server_2);

            // To Do
            // What to do with the msg,
            // don't know what type of message is required
            // therefore keep Biginteger, but can be changed accordingly




        }catch (Exception e){
            System.out.println(e);
        }
        return decrypt_msg_from_server_2;
    }
    public ElGamalMessage[] send_Elgamal_msg_to_Server3_for_decryption(String message){
        String drycpt="get_messege";

        // replace this value with the appropriate Elgamal_msg type
        //ElGamalMessage m1=null;
        try{
            //calling interface function "decrypt_messege" on server 2
            decrypt_msg_from_server_3=ElGamalBitMessageConversion.StringToElgamalBitMessage(call_server3.decrypt_messege(message,drycpt));
            System.out.println("Calling Server 3 from remote Decryption: "+decrypt_msg_from_server_2);

            // To Do
            // What to do with the msg,
            // don't know what type of message is required
            // therefore keep Biginteger, but can be changed accordingly


        }catch (Exception e){
            System.out.println(e);
        }
        return decrypt_msg_from_server_3;
    }



    /* This method is called when the program is run from the command line. */
    public static void main (String argv[]) throws Exception {
        /* Create a SimpleWebServer object, and run it */
        SimpleWebServer sws = new SimpleWebServer();

        // run the coordinator server (this server)
        sws.run();




    }
}
