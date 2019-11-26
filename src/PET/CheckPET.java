package PET;

import edu.boisestate.elgamal.ElGamal;
import edu.boisestate.elgamal.ElGamalMessage;
import edu.boisestate.elgamal.ElGamalPrivateKey;

import java.math.BigInteger;

public class CheckPET {

   public boolean checkEqualityOfTwoMessage(ElGamalMessage m1, ElGamalMessage m2, ElGamalPrivateKey privateKey){

       System.out.println("PET parameters: " + ElGamal.decryptMessage(m1,privateKey) + ", " + ElGamal.decryptMessage(m2,privateKey));

        BigInteger alphaMessage1, betaMessage1,alphaMessage2, betaMessage2, divideAlpha, divideBeta, decryptmessage;

        // get alpha and beta for message 1
        alphaMessage1 =m1.getEncryptedMessage();
        betaMessage1 =m1.getEphimeralKey();

       // get alpha and beta for message 2
        alphaMessage2 = m2.getEncryptedMessage();
        betaMessage2 = m2.getEphimeralKey();

        //System.out.println("first message == " + m1.getEncryptedMessage() + ", " + m1.getEphimeralKey());
        //System.out.println("second message == " + m2.getEncryptedMessage() + ", " + m2.getEphimeralKey());
        //Divide m1/m2
       if(alphaMessage1.compareTo(alphaMessage2) == 1)
       {
           divideAlpha = alphaMessage1.divide(alphaMessage2);
           //divideBeta =betaMessage1.divide(betaMessage2);
       }
       else
       {
           divideAlpha = alphaMessage2.divide(alphaMessage1);
           //divideBeta =betaMessage2.divide(betaMessage1);
       }
       if(betaMessage1.compareTo(betaMessage2) == 1)
       {
           divideBeta = betaMessage1.divide(betaMessage2);
       }
       else
       {
           divideBeta = betaMessage2.divide(betaMessage1);
       }


        //System.out.println("divideAlpha == " + divideAlpha);
        //System.out.println("divideBeta == " + divideBeta);

        ElGamalMessage newelgamal=new ElGamalMessage(divideBeta, divideAlpha);

        System.out.println("PET before decryption == " + newelgamal.getEncryptedMessage() + ", " + newelgamal.getEphimeralKey());
        //decreypt result
       decryptmessage=ElGamal.decryptMessage(newelgamal, privateKey);
       System.out.println("pet total decryption = " + decryptmessage);
       //System.out.println("decrypted message == " + decryptmessage);
       if (decryptmessage.equals(BigInteger.valueOf(1))){
           return true;
       }
       return false;
















    }
}
