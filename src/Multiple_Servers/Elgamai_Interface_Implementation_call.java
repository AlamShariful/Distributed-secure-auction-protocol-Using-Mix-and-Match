package Multiple_Servers;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.*;
import java.math.BigInteger;
import edu.boisestate.elgamal.*;


public class Elgamai_Interface_Implementation_call extends UnicastRemoteObject implements Elgamal_interface {
//public class Elgamai_Interface_Implementation_call implements Elgamal_interface {

    BigInteger result=null;
    BigInteger elgamal_msg = null;
    //String rtn ="Finally";
    public static ElGamalPrivateKey privateKey= new ElGamalPrivateKey();
    ElGamalPublicKey publicKey = new ElGamalPublicKey();

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
    public ElGamalPrivateKey generate_privatekey(String search_privateKey, int bits, BigInteger p, BigInteger g) throws RemoteException{
        // return Elgamal Private key

        if(search_privateKey.equals("get_privateKey")){

            //call Elgamal package to get private key
            privateKey = ElGamal.generateKeyPair(bits, p, g);
            result = privateKey.getPrivateKey();
            System.out.println("Private Key: "+result);

            //get public key
            publicKey = privateKey.getPublicKey();
            System.out.println("Public key: "+publicKey.getP()+","+publicKey.getG()+","+publicKey.getB());
        }
        //return result;
        return privateKey;
    }

    // decrypting msg

    /*
    @Override
    public BigInteger decrypt_messege (ElGamalMessage msg, ElGamalPrivateKey privateKey, String decrypt_msg) throws RemoteException{
        BigInteger elgamal_msg = ElGamal.decryptMessage(msg,privateKey);

        return elgamal_msg;
    }
     */
    @Override
    public BigInteger decrypt_messege (ElGamalMessage msg, String decrypt_msg) throws RemoteException{
        if(decrypt_msg.equals("get_messege")){
            //System.out.println("Server 2 Private Key:"+ privateKey.getPrivateKey());
            elgamal_msg = ElGamal.decryptMessage(msg,privateKey);
        }else{
            System.out.println("Inside Else Block");
        }
        //return rtn;
        return elgamal_msg;
    }




}
