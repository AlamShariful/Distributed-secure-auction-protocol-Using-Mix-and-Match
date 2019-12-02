package otherFunctions;

import edu.boisestate.elgamal.ElGamal;
import edu.boisestate.elgamal.ElGamalPublicKey;

import java.math.BigInteger;

public class BitResultHandler
{
    public static String normalizeBitString(String bid)
    {
        String result = "";
        String ZeroAlt = ElGamal.GetZeroAlternative().toString();

        for(int i=0;i<bid.length();i++)
        {
            if(bid.charAt(i) == ZeroAlt.charAt(0))
            {
                result = result + "0";
            }
            else
            {
                result = result + "1";
            }
        }
        return result;
    }
    public static BigInteger BitStringToDecimalBigInteger(String bits)
    {
        BigInteger result = new BigInteger(bits,2);
        return  result;
    }
}
