package Multiple_Servers;

import edu.boisestate.elgamal.*;

import java.math.BigInteger;
import java.rmi.Naming;

public class Client2 {
    public static void main(String args[])
    {
        ElGamalPrivateKey answer;
        String value="get_privateKey";
        try
        {
            // lookup method to find reference of remote object
            Elgamal_interface privateKey1 =
                    (Elgamal_interface) Naming.lookup("rmi://localhost:1900"+
                            "/privateKey");
            int bits = 20;
            BigInteger p = ElGamal.generateSafePrime(bits);
            BigInteger g = ElGamal.generateRandomPrimitive(p);

            answer = privateKey1.generate_privatekey(value, bits, p, g);
            //answer2 = privateKey.query2(answer2);
            //System.out.println("From Elgamal_Interface__Implementation_call Private Key: "+answer);
            System.out.println("From Elgamal_Interface__Implementation_call Private Key: "+answer.getPrivateKey());
            System.out.println("From Elgamal_Interface__Implementation_call Public Key: "+answer.getPublicKey().getG());
        }
        catch(Exception ae)
        {
            System.out.println(ae);
        }
    }
}
