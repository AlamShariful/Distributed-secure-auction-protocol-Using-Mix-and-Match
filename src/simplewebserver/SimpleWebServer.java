package simplewebserver;


import GreaterThanFunction.GreaterThanFunction;
import Multiple_Servers.Elgamal_interface;
import edu.boisestate.elgamal.*;
import otherFunctions.BitResultHandler;
import otherFunctions.ElGamalBitMessageConversion;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import static java.nio.file.StandardOpenOption.APPEND;
import java.nio.file.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;
//import com.google.gson.*;

public class SimpleWebServer{

    /* Run the HTTP server on this TCP port. */
    //private static final int PORT = 8089;
    private static final int [] PORT = new int []{8089, 8000, 4789, 6589};
    //private static BigInteger server1_publicKey,server2_publicKey, server3_publicKey,server1_privateKey,server2_privateKey,server3_privateKey;
    private static ElGamalPublicKey server1_publicKey,server2_publicKey, server3_publicKey,commonPublicKey;
    private static ElGamalPrivateKey server1_privateKey,server2_privateKey,server3_privateKey;
    public static String msg_Client1,msg_Client2,msg_Client3,msg_Client4;
    public ElGamalMessage [] decrypt_msg_from_server_2,decrypt_msg_from_server_3;


    //Client Connecting
    private OutputStream outputStream;
    private ObjectOutputStream obj1,obj2,obj3,obj4;
    private DataInputStream dis;



    //Interface
    public Elgamal_interface call_server2,call_server3;


    //List for Holding Grouppublic key
    List<ElGamalPublicKey> groupPublicKey = new ArrayList<ElGamalPublicKey>();

    //public static int i=0;

    private int bits = 20;
    private BigInteger p = ElGamal.generateSafePrime(bits);
    private BigInteger g = ElGamal.generateRandomPrimitive(p);

    //all biddings
    private ElGamalMessage [][] allBiddings = new ElGamalMessage[4][];
    private int allBiddingCounter = 0;

    //greater than table
    private GreaterThanFunction greaterThanFunction;

    /* The socket used to process incoming connections from web clients */
    //private static ServerSocket dServerSocket;
    private static ServerSocket dServerSocket1;
    private static ServerSocket dServerSocket2;
    private static ServerSocket dServerSocket3;
    private static ServerSocket dServerSocket4;


    public SimpleWebServer () throws Exception {
        //dServerSocket = new ServerSocket (PORT);
        dServerSocket1 = new ServerSocket (PORT[0]);
        dServerSocket2 = new ServerSocket (PORT[1]);
        dServerSocket3 = new ServerSocket (PORT[2]);
        dServerSocket4 = new ServerSocket (PORT[3]);

        // calling remote servers
        calling_remote_Servers_interface();
        getting_publicKey_For_This_server();
        getting_publicKey_From_server2();
        getting_publicKey_From_server3();

        // test call to remote server 2/3,place it accordingly
        //send_Elgamal_msg_to_Server2_for_decryption();
        //send_Elgamal_msg_to_Server3_for_decryption();

        //get Group Public Key
        System.out.println("generating group public key.... ");
        commonPublicKey=ElGamal.getGroupPublicKey(groupPublicKey);
        System.out.println("group public key generated: " + commonPublicKey.getP()+","+commonPublicKey.getG()+","+commonPublicKey.getB());
    }

    public void run() throws Exception {

        // We are Condidering 4 Client, therefore this loop should Run 4 time (Connect 4 client)

        while (true) {
            int m=0;
            allBiddingCounter=0;

            while (m<4) {

                try{

                    /* wait for a connection from a client */
//                    Socket s = dServerSocket.accept();
//                    System.out.println("Client: "+Integer.sum(m,1)+" Connected");
//
//                    // Send output stream Object Via the socket. To client
//                    outputStream = s.getOutputStream();
//                    obj =new ObjectOutputStream(outputStream);
//                    obj.writeObject(commonPublicKey);
//
//                    // read Encrypted messege from User
//                    dis=new DataInputStream(s.getInputStream());
//                    String  str=(String)dis.readUTF();




                    //Assign Received Strings to Different Variables
                    if(m==0){
                        Socket s1 = dServerSocket1.accept();
                        System.out.println("Client: "+Integer.sum(m,1)+" Connected");

                        // Send output stream Object Via the socket. To client
                        outputStream = s1.getOutputStream();
                        obj1 =new ObjectOutputStream(outputStream);
                        obj1.writeObject(commonPublicKey);

                        // read Encrypted messege from User
                        dis=new DataInputStream(s1.getInputStream());
                        String  str=(String)dis.readUTF();


                        // Client 1 is sending Msg
                        msg_Client1=str;
                        System.out.println("Client 1 received Encrypted Message in string = " + msg_Client1);
                        ElGamalMessage [] bitMsg_client1 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client1);
                        allBiddings[allBiddingCounter] = new ElGamalMessage[bitMsg_client1.length];
                        allBiddings[allBiddingCounter] = bitMsg_client1;
                        allBiddingCounter++;
                        //System.out.println("equality test == " + msg_Client1.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client1)));
                    }
                    if(m==1){

                        Socket s = dServerSocket2.accept();
                        System.out.println("Client: "+Integer.sum(m,1)+" Connected");

                        // Send output stream Object Via the socket. To client
                        outputStream = s.getOutputStream();
                        obj2 =new ObjectOutputStream(outputStream);
                        obj2.writeObject(commonPublicKey);

                        // read Encrypted messege from User
                        dis=new DataInputStream(s.getInputStream());
                        String  str=(String)dis.readUTF();

                        // Client 2 is sending Msg
                        msg_Client2=str;
                        System.out.println("Client 2 received Encrypted Message in string = " + msg_Client2);
                        ElGamalMessage [] bitMsg_client2 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client2);
                        allBiddings[allBiddingCounter] = new ElGamalMessage[bitMsg_client2.length];
                        allBiddings[allBiddingCounter] = bitMsg_client2;
                        allBiddingCounter++;
                        //System.out.println("equality test == " + msg_Client2.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client2)));
                    }
                    if(m==2){

                        Socket s = dServerSocket3.accept();
                        System.out.println("Client: "+Integer.sum(m,1)+" Connected");

                        // Send output stream Object Via the socket. To client
                        outputStream = s.getOutputStream();
                        obj3 =new ObjectOutputStream(outputStream);
                        obj3.writeObject(commonPublicKey);

                        // read Encrypted messege from User
                        dis=new DataInputStream(s.getInputStream());
                        String  str=(String)dis.readUTF();

                        // Client 3 is sending Msg
                        msg_Client3=str;
                        System.out.println("Client 3 received Encrypted Message in string = " + msg_Client3);
                        ElGamalMessage [] bitMsg_client3 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client3);
                        allBiddings[allBiddingCounter] = new ElGamalMessage[bitMsg_client3.length];
                        allBiddings[allBiddingCounter] = bitMsg_client3;
                        allBiddingCounter++;
                        //System.out.println("equality test == " + msg_Client3.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client3)));
                    }
                    if(m==3){

                        Socket s = dServerSocket4.accept();
                        System.out.println("Client: "+Integer.sum(m,1)+" Connected");

                        // Send output stream Object Via the socket. To client
                        outputStream = s.getOutputStream();
                        obj4 =new ObjectOutputStream(outputStream);
                        obj4.writeObject(commonPublicKey);

                        // read Encrypted messege from User
                        dis=new DataInputStream(s.getInputStream());
                        String  str=(String)dis.readUTF();

                        // Client 4 is sending Msg
                        msg_Client4=str;
                        System.out.println("Client 4 received Encrypted Message in string = " + msg_Client4);
                        ElGamalMessage [] bitMsg_client4 = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg_Client4);
                        allBiddings[allBiddingCounter] = new ElGamalMessage[bitMsg_client4.length];
                        allBiddings[allBiddingCounter] = bitMsg_client4;
                        allBiddingCounter++;
                        //System.out.println("equality test == " + msg_Client4.equals(ElGamalBitMessageConversion.ElgamalBitMessageToString(bitMsg_client4)));
                    }


                    //Count Connected Client
                    m++;



                }catch (Exception e){System.out.println(e);}
            }



            System.out.println("4 messages collected. Messages are: \n" + msg_Client1 + "\n" + msg_Client2 + "\n"+ msg_Client3 + "\n"+ msg_Client4 + "\n");
            //System.out.println("checking equality with global storage:");
            //System.out.println(ElGamalBitMessageConversion.ElgamalBitMessageToString(allBiddings[0]).equals(msg_Client1) + ", " + ElGamalBitMessageConversion.ElgamalBitMessageToString(allBiddings[1]).equals(msg_Client2) + ", " + ElGamalBitMessageConversion.ElgamalBitMessageToString(allBiddings[2]).equals(msg_Client3) + ", " + ElGamalBitMessageConversion.ElgamalBitMessageToString(allBiddings[3]).equals(msg_Client4));

            //initialize greater than table
            initializeGreaterThanTable();
            //we take bit by bit of pair users
            ElGamalMessage [] result = allBiddings[0];
            for(int i=1;i<allBiddingCounter;i++)
            {
                result = generatePairTableAndFindMax(result, allBiddings[i]);
                greaterThanFunction.ReShuffleAndReEncryptTable(commonPublicKey);
            }

            System.out.println("winner cipher text found!!, Decrypting bid: ");
            BigInteger winner = decryptWinningBid(result);

            System.out.println("winner == " + winner);


            // Send Winning Bid ConnectedClient
            String winingBid= winner.toString();
            System.out.println("Sending Winning Bid to Client");
            obj1.writeObject(winingBid);
            obj2.writeObject(winingBid);
            obj3.writeObject(winingBid);
            obj4.writeObject(winingBid);




            // END


                // reset the While Loop

            }
        }
        private ElGamalMessage [] generatePairTableAndFindMax(ElGamalMessage [] m1, ElGamalMessage [] m2) throws RemoteException {
            ElGamalMessage [] m1Prime = new ElGamalMessage[m1.length + 1];

            ElGamalMessage [] m2Prime = new ElGamalMessage[m2.length + 1];

            for(int i=0;i<1024;i++)
            {
                m1Prime[i+1] = m1[i];
                m2Prime[i+1] = m2[i];
            }

            if(findGreater(m1, m2))
            {
                m1Prime[0] = ElGamal.encryptMessage(commonPublicKey, ElGamal.GetOne());
                m2Prime[0] = ElGamal.encryptMessage(commonPublicKey, ElGamal.GetNegOneAlternative());
            }
            else
            {
                m2Prime[0] = ElGamal.encryptMessage(commonPublicKey, ElGamal.GetOne());
                m1Prime[0] = ElGamal.encryptMessage(commonPublicKey, ElGamal.GetNegOneAlternative());
            }
            m1Prime = reEncryptAMessage(m1Prime);
            m2Prime = reEncryptAMessage(m2Prime);

            if(findGreater(m1,m2))
            {
                return m1;
            }
            return m2;

        }
        private ElGamalMessage [] reEncryptAMessage(ElGamalMessage [] m1) {
            ElGamalMessage [] result = m1;
            for(int i=0;i<m1.length;i++)
            {
                result[i] = ElGamal.reEncryptMessage(result[i], commonPublicKey);
            }
            return result;
        }
        private void initializeGreaterThanTable() {
            //prepare greater than table
            System.out.println("Generating Greater than table");
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
            System.out.println("re-shuffling the table:");
            greaterThanFunction.ReShuffleAndReEncryptTable(commonPublicKey);
            greaterThanFunction.PrintTable();

            greaterThanFunction.saveServerInstance(this);
        }
        private boolean findGreater(ElGamalMessage [] bid1, ElGamalMessage [] bid2) throws RemoteException {
            //execute pairwuse comparison
            /*if(greaterThanFunction.CheckDistributedGreater(bid1, bid2))
            {
                return bid1;
            }
            else if(greaterThanFunction.CheckDistributedGreater(bid2, bid1))
            {
                return bid2;
            }
            else
            {
                return bid1;
            }*/
            if(greaterThanFunction.CheckDistributedGreater(bid1, bid2))
            {
                return true;
            }
            return false;
        }
        private BigInteger decryptWinningBid(ElGamalMessage [] result) {
            BigInteger resultPlainText;
            ElGamalMessage [] decryptedMessage = result;
            String decryptedMessageString;
            //main server
            decryptedMessage = ElGamal.partialBitbyBitDecryption(result, server1_privateKey);

            //second server
            decryptedMessage = send_Elgamal_msg_to_Server2_for_decryption(ElGamalBitMessageConversion.ElgamalBitMessageToString(decryptedMessage));
            //third server
            decryptedMessage = send_Elgamal_msg_to_Server3_for_decryption(ElGamalBitMessageConversion.ElgamalBitMessageToString(decryptedMessage));

            String resultBid = ElGamal.getStringToBitbyBitDecryption(decryptedMessage);

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

                //System.out.println("Server 2 Private Key: "+server2_privateKey.getPrivateKey());
                //System.out.println("Server 2 Public key: "+server2_publicKey.getP()+","+server2_publicKey.getG()+","+server2_publicKey.getB());
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

                //System.out.println("Server 3 Private Key: "+server3_privateKey.getPrivateKey());
               // System.out.println("Server 3 Public key: "+server3_publicKey.getP()+","+server3_publicKey.getG()+","+server3_publicKey.getB());
            }
            catch(Exception ae)
            {
                System.out.println(ae);
            }
        }

        public ElGamalMessage [] send_Elgamal_msg_to_Server2_for_decryption(String message){
            String drycpt="get_messege";

            // replace this value with the appropriate Elgamal_msg type
            //ElGamalMessage m1=null;
            try{
                //calling interface function "decrypt_messege" on server 2
                decrypt_msg_from_server_2= ElGamalBitMessageConversion.StringToElgamalBitMessage(call_server2.decrypt_messege(message,drycpt));
                System.out.println("Calling Server 2 for remote Decryption: "+decrypt_msg_from_server_2);

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
                System.out.println("Calling Server 3 for remote Decryption: "+decrypt_msg_from_server_2);

                // To Do
                // What to do with the msg,
                // don't know what type of message is required
                // therefore keep Biginteger, but can be changed accordingly


            }catch (Exception e){
                System.out.println(e);
            }
            return decrypt_msg_from_server_3;
        }

        public ElGamalMessage getDivision(ElGamalMessage m1, ElGamalMessage m2) throws RemoteException {
        BigInteger alphaMessage1, betaMessage1,alphaMessage2, betaMessage2, divideAlpha, divideBeta;

        // get alpha and beta for message 1
        alphaMessage1 =m1.getEncryptedMessage();
        betaMessage1 =m1.getEphimeralKey();

        // get alpha and beta for message 2
        alphaMessage2 = m2.getEncryptedMessage();
        betaMessage2 = m2.getEphimeralKey();
        BigInteger alphaMessage2Inverse = ElGamal.extendedEuclidAlgorithm(server1_privateKey.getPublicKey().getP(), alphaMessage2).getT();
        BigInteger betaMessage2Inverse = ElGamal.extendedEuclidAlgorithm(server1_privateKey.getPublicKey().getP(), betaMessage2).getT();

        divideAlpha = (alphaMessage1.multiply(alphaMessage2Inverse)).mod(server1_privateKey.getPublicKey().getP());
        divideBeta = (betaMessage1.multiply(betaMessage2Inverse)).mod(server1_privateKey.getPublicKey().getP());

        ElGamalMessage newelgamal=new ElGamalMessage(divideBeta, divideAlpha);

        return newelgamal;
    }
        public ElGamalMessage decrypt_messege (ElGamalMessage msg) throws RemoteException {
            ElGamalMessage elgMsg = ElGamal.decryptGroupMessage(msg, server1_privateKey);
            //partially decrypt the message
            return elgMsg;
        }


        /* This method is called when the program is run from the command line. */
        public static void main (String argv[]) throws Exception {
            /* Create a SimpleWebServer object, and run it */
            SimpleWebServer sws = new SimpleWebServer();

            // run the coordinator server (this server)
            sws.run();

        }
}
