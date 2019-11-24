package Tables;

import edu.boisestate.elgamal.ElGamalMessage;

import java.math.BigInteger;

public class SenderEncryptionTableRow {
    private ElGamalMessage[] input;

    public SenderEncryptionTableRow()
    {
        input = new ElGamalMessage[1024];
    }

    public void setInput(ElGamalMessage input, int index)
    {
        this.input[index] = input;
    }

    public ElGamalMessage[] getInput()
    {
        return this.input;
    }

}
