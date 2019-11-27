// Java program to implement the Search interface
package Test_RMI_Multiple_Server;

import java.rmi.*;
import java.rmi.server.*;

public class SearchQuery extends UnicastRemoteObject implements Search {

    // Default constructor to throw RemoteException
    // from its parent constructor
    SearchQuery() throws RemoteException
    {
        super();
    }

    // Implementation of the query interface
    @Override
    public String query(String search)
            throws RemoteException
    {
        String result;
        if (search.equals("Reflection in Java"))
            result = "Found";
        else
            result = "Not Found";

        return result;
    }
    // implement second method
    @Override
    public String query2(String search)
            throws RemoteException
    {
        String result;
        if (search.equals("Call other"))
            result = "From second method";
        else
            result = "Not Found";

        return result;
    }

}
