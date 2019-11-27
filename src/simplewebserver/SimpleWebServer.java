package simplewebserver;

import Test_RMI_Multiple_Server.Search;
import Test_RMI_Multiple_Server.SearchQuery;

import java.io.*;
import java.net.*;
import static java.nio.file.StandardOpenOption.APPEND;
import java.nio.file.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class SimpleWebServer {

    /* Run the HTTP server on this TCP port. */
    private static final int PORT = 8089;

    /* The socket used to process incoming connections from web clients */
    private static ServerSocket dServerSocket;

    public SimpleWebServer () throws Exception {
        dServerSocket = new ServerSocket (PORT);
    }

    public void run() throws Exception {
        while (true) {

            try{
                /* wait for a connection from a client */
                Socket s = dServerSocket.accept();
                System.out.println("Client Connected");

                // read data from socket
                DataInputStream dis=new DataInputStream(s.getInputStream());
                String  str=(String)dis.readUTF();
                System.out.println("message from Client= "+str);

                // calling remote server
                test_remote();

                // calling remote server ends here

            }catch (Exception e){System.out.println(e);}
        }
    }

    // calling remote server to additional support
    public void test_remote(){
        String answer,value="Reflection in Java";
        String answer2 = "Call other";
        try
        {
            // lookup method to find reference of remote object
            Search access =
                    (Search)Naming.lookup("rmi://localhost:1900"+
                            "/geeksforgeeks");
            answer = access.query(value);

            // calling secondserver
            Search access2 =
                    (Search)Naming.lookup("rmi://localhost:6457"+
                    "/server");

            answer2 = access2.query2(answer2);
            System.out.println("Article on " + value +
                    " " + answer+" at GeeksforGeeks"+answer2);
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
        sws.run();

    }
}
