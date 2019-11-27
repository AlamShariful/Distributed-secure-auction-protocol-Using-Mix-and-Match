package Test_RMI_Multiple_Server;

import java.rmi.*;

public interface Search extends Remote {
    // Declaring the method prototype
    public String query(String search) throws RemoteException;
    public String query2(String search) throws RemoteException;

}
