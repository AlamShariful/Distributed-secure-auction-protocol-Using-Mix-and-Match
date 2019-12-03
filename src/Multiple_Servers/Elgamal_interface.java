package Multiple_Servers;
import java.rmi.*;
import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;
import PET.CheckPET;
import edu.boisestate.elgamal.*;

public interface Elgamal_interface extends Remote {

    public ElGamalPrivateKey generate_privatekey(String search_privateKey, int bits, BigInteger p, BigInteger g) throws RemoteException;
    //public BigInteger decrypt_messege (ElGamalMessage msg, ElGamalPrivateKey privatekey, String decrypt_msg) throws RemoteException;

    public ElGamalMessage [] decrypt_messege (ElGamalMessage [] msg, String decrypt_msg) throws RemoteException;
}
