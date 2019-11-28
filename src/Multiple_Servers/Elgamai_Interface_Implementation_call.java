package Multiple_Servers;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.*;
import java.math.BigInteger;
import edu.boisestate.elgamal.*;


public class Elgamai_Interface_Implementation_call extends UnicastRemoteObject implements Elgamal_interface {
//public class Elgamai_Interface_Implementation_call implements Elgamal_interface {
    // Default constructor to throw RemoteException
    // from its parent constructor
    private static final long serialVersionUID = 1L;
    Elgamai_Interface_Implementation_call() throws RemoteException {
        super();
    }

    /* Call Elgamal Key generation from Elgamal package
    // implement Elgamal_interface
    // Generate Elgamal Private Key
     */

    @Override
    //public BigInteger generate_privatekey(String search_privateKey) throws RemoteException{
    public ElGamalPrivateKey generate_privatekey(String search_privateKey) throws RemoteException{
        // return Elgamal Private key
        BigInteger result=null;
        ElGamalPrivateKey privateKey= new ElGamalPrivateKey();
        ElGamalPublicKey publicKey = new ElGamalPublicKey();
        if(search_privateKey.equals("get_privateKey")){

            //call Elgamal package to get private key
            privateKey = ElGamal.generateKeyPair(12);
            result = privateKey.getPrivateKey();
            System.out.println("Private Key: "+result);

            //get public key
            publicKey = privateKey.getPublicKey();
            System.out.println("Public key: "+publicKey);
        }
        //return result;
        return privateKey;
    }

    // generate Elgamal Public key
    public BigInteger generate_publicekey(String search_publicKey) throws RemoteException{
        // return Elgamal Publick key
        BigInteger result=null;
        if(search_publicKey.equals("get_publickey")){
            //call Elgamal package to get public key
            ElGamalPublicKey publicKey;
            //publicKey = privateKey.getPublicKey();


        }
        return result;
    }

    // return both private key and public key
    public BigInteger[] generate_keypair(String search_keypair) throws RemoteException{
        BigInteger[] result=null;

        if(search_keypair.equals("get_keypair")){

            //call Elgamal package to get private key
            ElGamalPrivateKey privateKey = ElGamal.generateKeyPair(12);
            result[0] = privateKey.getPrivateKey();
            System.out.println("Private Key: "+result[0]);

            //call Elgamal package to get public key
            ElGamalPublicKey publicKey;
            publicKey = privateKey.getPublicKey();
            //result[1]=publicKey;



        }

        return result;
    }

}
