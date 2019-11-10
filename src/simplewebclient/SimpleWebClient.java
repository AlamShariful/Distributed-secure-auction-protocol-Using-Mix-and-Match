package simplewebclient;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

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
            String userInput;
            if ((userInput = stdIn.readLine()) != null) {
                out.writeUTF(userInput);
                out.flush();
                out.close();
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
