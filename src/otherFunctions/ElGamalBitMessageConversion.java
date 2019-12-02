package otherFunctions;

import edu.boisestate.elgamal.ElGamalMessage;

import java.math.BigInteger;
import java.util.StringTokenizer;

public class ElGamalBitMessageConversion
{
    public static String ElgamalBitMessageToString(ElGamalMessage[] inputBits)
    {
        String ciphertext="";
        for (int i=0; i<inputBits.length;i++){
            if (ciphertext == ""){
                ciphertext=inputBits[i].getEncryptedMessage().toString() + "," + inputBits[i].getEphimeralKey().toString();
            }else{
                ciphertext=ciphertext +"."+inputBits[i].getEncryptedMessage().toString() + "," + inputBits[i].getEphimeralKey().toString();
            }
        }
        return  ciphertext;
    }
    public static ElGamalMessage[] StringToElgamalBitMessage(String cipherText)
    {
        StringTokenizer tokenizer = new StringTokenizer(cipherText, ".");

        int totalTokens = tokenizer.countTokens();

        ElGamalMessage [] inputBits = new ElGamalMessage[totalTokens];

        String message, ephemeralKey;
        BigInteger bigMessage, bigEphemeralKey;
        int counter = 0;
        while (tokenizer.hasMoreTokens())
        {
            if(counter == totalTokens) break;

            String temp = tokenizer.nextToken();

            StringTokenizer tempTokenizer = new StringTokenizer(temp, ",");
            if(tempTokenizer.hasMoreTokens())
            {
                message = tempTokenizer.nextToken();
            }
            else
            {
                message = null;
            }
            if(tempTokenizer.hasMoreTokens())
            {
                ephemeralKey = tempTokenizer.nextToken();
            }
            else
            {
                ephemeralKey = null;
            }
            bigMessage = new BigInteger(message);

            bigEphemeralKey = new BigInteger(ephemeralKey);

            inputBits[counter] = new ElGamalMessage(bigEphemeralKey, bigMessage);

            counter++;
        }

        return inputBits;
    }
}
