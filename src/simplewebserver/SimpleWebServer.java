package simplewebserver;

import Multiple_Servers.Elgamal_interface;
import Test_RMI_Multiple_Server.Search;
import Test_RMI_Multiple_Server.SearchQuery;
import edu.boisestate.elgamal.ElGamal;
import edu.boisestate.elgamal.ElGamalPrivateKey;
import edu.boisestate.elgamal.ElGamalPublicKey;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import static java.nio.file.StandardOpenOption.APPEND;
import java.nio.file.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class SimpleWebServer {

    /* Run the HTTP server on this TCP port. */
    private static final int PORT = 8089;
    private static BigInteger server1_publicKey,server2_publicKey, server3_publicKey,server1_privateKey,server2_privateKey,server3_privateKey;

    /* The socket used to process incoming connections from web clients */
    private static ServerSocket dServerSocket;

    public SimpleWebServer () throws Exception {
        dServerSocket = new ServerSocket (PORT);
        getting_publicKey_For_This_server();
        getting_publicKey_From_server2();
        getting_publicKey_From_server3();
    }

    public void run() throws Exception {
        while (true) {

            try{
                /* wait for a connection from a client */
                Socket s = dServerSocket.accept();
                System.out.println("Client Connected");

                //Send common publicKey to Client Via socket
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                //Change server1_publicKey with common publicKey and convert BigInteger to String
                out.writeUTF(server1_publicKey.toString());
                out.flush();

                // read Encrypted messege from User
                DataInputStream dis=new DataInputStream(s.getInputStream());
                String  str=(String)dis.readUTF();
                System.out.println("Encrypted Message from Client= "+str);


            }catch (Exception e){System.out.println(e);}
        }
    }

    // calling remote server to additional support
    public void getting_publicKey_For_This_server(){
        BigInteger result=null;
        ElGamalPrivateKey privateKey= new ElGamalPrivateKey();


        //call Elgamal package to get private key
        privateKey = ElGamal.generateKeyPair(12);

        //private key
        server1_privateKey=privateKey.getPrivateKey();

        //get public key
        server1_publicKey= privateKey.getPublicKey().getG();


        System.out.println("Server 1 Private Key: "+server1_privateKey);
        System.out.println("Server 1 Public key: "+server1_publicKey);

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

            answer = privateKey1.generate_privatekey(value);

            // server 2 key pair
            server2_publicKey=answer.getPublicKey().getG();
            server2_privateKey=answer.getPrivateKey();

            //System.out.println("From Elgamal_Interface__Implementation_call Private Key: "+answer.getPrivateKey());
            //System.out.println("From Elgamal_Interface__Implementation_call Public Key: "+answer.getPublicKey().getG());

            System.out.println("Server 2 Private Key: "+server2_privateKey);
            System.out.println("Server 2 Public key: "+server2_publicKey);
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
            answer = privateKey1.generate_privatekey(value);

            // server 3 keypair
            server3_publicKey=answer.getPublicKey().getG();
            server3_privateKey=answer.getPrivateKey();

            //System.out.println("From Elgamal_Interface__Implementation_call Private Key: "+answer.getPrivateKey());
            //System.out.println("From Elgamal_Interface__Implementation_call Public Key: "+answer.getPublicKey().getG());

            System.out.println("Server 3 Private Key: "+server3_privateKey);
            System.out.println("Server 3 Public key: "+server3_publicKey);
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

        // run function to get a common public key

        // run the coordinator server (this server)
        sws.run();



    }
}
