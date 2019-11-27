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

    public BigInteger generate_privatekey(String search_privateKey) throws RemoteException;
    public BigInteger generate_publicekey(String search_publicKey) throws RemoteException;
    public BigInteger[] generate_keypair(String search_keypair) throws RemoteException;

}
