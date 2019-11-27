package Test_RMI_Multiple_Server;

import java.rmi.*;
import java.rmi.registry.*;

public class server2 {
    server2() throws RemoteException{
        System.out.println("Client2 Connected");
    }
    public static void main(String args[])
    {
        try
        {
            // Create an object of the interface
            // implementation class
            Search obj = new SearchQuery();

            // rmiregistry within the server JVM with
            // port number 1900
            LocateRegistry.createRegistry(6457);

            // Binds the remote object by the name
            // geeksforgeeks
            Naming.rebind("rmi://localhost:6457"+
                    "/server",obj);
        }
        catch(Exception ae)
        {
            System.out.println(ae+"Second server");
        }
    }
}
