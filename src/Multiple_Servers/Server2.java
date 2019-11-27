package Multiple_Servers;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server2 {
    public static void main(String args[])
    {
        try
        {
            // Create an object of the interface
            // implementation class
            Elgamai_Interface_Implementation_call obj = new Elgamai_Interface_Implementation_call();

            // rmiregistry within the server JVM with
            // port number 1900
            LocateRegistry.createRegistry(1900);

            // Binds the remote object by the name
            // geeksforgeeks
            Naming.rebind("rmi://localhost:1900"+
                    "/geeksforgeeks",obj);
        }
        catch(Exception ae)
        {
            System.out.println(ae);
        }
    }
}
