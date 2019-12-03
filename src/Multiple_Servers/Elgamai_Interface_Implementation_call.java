package Multiple_Servers;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.*;
import java.math.BigInteger;

import edu.boisestate.elgamal.*;
import otherFunctions.ElGamalBitMessageConversion;


public class Elgamai_Interface_Implementation_call extends UnicastRemoteObject implements Elgamal_interface {
//public class Elgamai_Interface_Implementation_call implements Elgamal_interface {

    BigInteger result=null;
    ElGamalMessage [] elgamal_msg = null;
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
    public String decrypt_messege (String msg, String decrypt_msg) throws RemoteException{
        if(decrypt_msg.equals("get_messege")){

            /**
             * remote servers 2 and 3 use this function for their partial decryption
             * Sending and returning Elgamal message requires serialization, therefore the
             * flow is done using strings
             */
            System.out.println("Remote Server Applying Partial Decryption");

            //first convert the message back to ElGamal message
            ElGamalMessage [] elgMsg = ElGamalBitMessageConversion.StringToElgamalBitMessage(msg);
            //partially decrypt the message
            elgamal_msg = ElGamal.partialBitbyBitDecryption(elgMsg,privateKey);
            System.out.println("Partially decrypted cipher text: " + ElGamalBitMessageConversion.ElgamalBitMessageToString(elgamal_msg));
        }else{
            System.out.println("Inside Else Block");
        }

        //now return the message as string again;
        return ElGamalBitMessageConversion.ElgamalBitMessageToString(elgamal_msg);
    }


    public boolean checkEqualityOfTwoMessage(ElGamalMessage m1, ElGamalMessage m2, ElGamalPrivateKey privateKey) throws RemoteException{
        BigInteger alphaMessage1, betaMessage1,alphaMessage2, betaMessage2, divideAlpha, divideBeta, decryptmessage;

        // get alpha and beta for message 1
        alphaMessage1 =m1.getEncryptedMessage();
        betaMessage1 =m1.getEphimeralKey();

        // get alpha and beta for message 2
        alphaMessage2 = m2.getEncryptedMessage();
        betaMessage2 = m2.getEphimeralKey();
        BigInteger alphaMessage2Inverse = ElGamal.extendedEuclidAlgorithm(privateKey.getPublicKey().getP(), alphaMessage2).getT();
        BigInteger betaMessage2Inverse = ElGamal.extendedEuclidAlgorithm(privateKey.getPublicKey().getP(), betaMessage2).getT();

        divideAlpha = (alphaMessage1.multiply(alphaMessage2Inverse)).mod(privateKey.getPublicKey().getP());
        divideBeta = (betaMessage1.multiply(betaMessage2Inverse)).mod(privateKey.getPublicKey().getP());

        ElGamalMessage newelgamal=new ElGamalMessage(divideBeta, divideAlpha);
        decryptmessage=ElGamal.decryptMessage(newelgamal, privateKey);

        if (decryptmessage.equals(BigInteger.valueOf(1)))
        {
            return true;
        }
        return false;
    }


}
