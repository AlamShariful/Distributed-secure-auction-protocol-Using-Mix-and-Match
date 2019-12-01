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

    // decrypting msg
    @Override
    public BigInteger decrypt_messege (ElGamalMessage msg, ElGamalPrivateKey privateKey, String decrypt_msg) throws RemoteException{
        BigInteger elgamal_msg = ElGamal.decryptMessage(msg,privateKey);

        return elgamal_msg;
    }



}
