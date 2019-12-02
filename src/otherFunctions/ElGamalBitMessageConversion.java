package otherFunctions;

import edu.boisestate.elgamal.ElGamalMessage;

import java.math.BigInteger;

public class ElGamalBitMessageConversion
{
    public static String ElgamalBitMessageToString(ElGamalMessage[] inputBits)
    {
        String ciphertext="";
        for (int i=0; i<inputBits.length;i++){
            System.out.println("Table Output"+inputBits[i].getEncryptedMessage() + " , " + inputBits[i].getEphimeralKey());
            //ciphertext=ciphertext.concat(getInputTable[i].toString());
            if (ciphertext == ""){
                ciphertext=inputBits[i].getEncryptedMessage().toString() + "," + inputBits[i].getEphimeralKey().toString();
            }else{
                ciphertext=ciphertext +"."+inputBits[i].getEncryptedMessage().toString() + "," + inputBits[i].getEphimeralKey().toString();
            }

            //System.out.println("Ciphertext"+ciphertext);
        }
        return  ciphertext;
    }
    public static ElGamalMessage[] StringToElgamalBitMessage(String cipherText)
    {
        //String.split
        String [] splittedData = cipherText.split(".");

        System.out.println("splitted size - " + splittedData.length);

        ElGamalMessage [] inputBits = new ElGamalMessage[splittedData.length];

        String [] fullMessage;
        String message, ephemeralKey;
        BigInteger bigMessage, bigEphemeralKey;

        for(int i=0;i<splittedData.length;i++)
        {
            fullMessage = splittedData[i].split(",");
            message = fullMessage[0];
            ephemeralKey = fullMessage[1];

            bigMessage = new BigInteger(message);
            inputBits[i].setEncryptedMessage(bigMessage);

            bigEphemeralKey = new BigInteger(ephemeralKey);
            inputBits[i].setEphimeralKey(bigEphemeralKey);
        }
        return inputBits;
    }
}
