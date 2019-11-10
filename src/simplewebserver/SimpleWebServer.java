package simplewebserver;

import java.io.*;
import java.net.*;
import static java.nio.file.StandardOpenOption.APPEND;
import java.nio.file.*;
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
            }catch (Exception e){System.out.println(e);}
        }
    }




    /* This method is called when the program is run from the command line. */
    public static void main (String argv[]) throws Exception {
        /* Create a SimpleWebServer object, and run it */
        SimpleWebServer sws = new SimpleWebServer();
        sws.run();
    }
}
