package Test_RMI_Multiple_Server;
//program for client application
import java.rmi.*;

public class ClientRequest {
    public static void main(String args[])
    {
        String answer,value="Reflection in Java";
        String answer2 = "Call other";
        try
        {
            // lookup method to find reference of remote object
            Search access =
                    (Search)Naming.lookup("rmi://localhost:1900"+
                            "/geeksforgeeks");
            answer = access.query(value);
            answer2 = access.query2(answer2);
            System.out.println("Article on " + value +
                    " " + answer+" at GeeksforGeeks"+answer2);
        }
        catch(Exception ae)
        {
            System.out.println(ae);
        }
    }
}
