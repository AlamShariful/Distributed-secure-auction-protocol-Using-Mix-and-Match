package Multiple_Servers;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server3 {
    public static void main(String args[])
    {
        try
        {
            // Create an object of the interface
            // implementation class
            Elgamal_interface obj = new Elgamai_Interface_Implementation_call();

            // rmiregistry within the server JVM with
            // port number 2000
            LocateRegistry.createRegistry(2000);

            // Binds the remote object by the name
            // privateKey
            Naming.rebind("rmi://localhost:2000"+
                    "/privateKey",obj);
        }
        catch(Exception ae)
        {
            System.out.println(ae);
        }
    }
}
