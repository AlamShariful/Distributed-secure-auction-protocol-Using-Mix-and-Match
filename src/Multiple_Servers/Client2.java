package Multiple_Servers;

import edu.boisestate.elgamal.*;

import java.math.BigInteger;
import java.rmi.Naming;

public class Client2 {
    public static void main(String args[])
    {
        //String answer,value="Reflection in Java";
        //String answer2 = "Call other";
        //BigInteger answer;
        ElGamalPrivateKey answer;
        String value="get_privateKey";
        try
        {
            // lookup method to find reference of remote object
            Elgamal_interface privateKey1 =
                    (Elgamal_interface) Naming.lookup("rmi://localhost:1900"+
                            "/privateKey");
            answer = privateKey1.generate_privatekey(value);
            //answer2 = privateKey.query2(answer2);
            //System.out.println("From Elgamal_Interface__Implementation_call Private Key: "+answer);
            System.out.println("From Elgamal_Interface__Implementation_call Private Key: "+answer.getPrivateKey());
            System.out.println("From Elgamal_Interface__Implementation_call Public Key: "+answer.getPublicKey());
        }
        catch(Exception ae)
        {
            System.out.println(ae);
        }
    }
}
