package Tables;

import java.math.BigInteger;

public class SenderEncryptionTableRow {
    private BigInteger[] input;

    public SenderEncryptionTableRow()
    {
        input = new BigInteger[1024];
    }

    public void setInput(BigInteger input, int index)
    {
        this.input[index] = input;
    }

    public BigInteger[] getInput()
    {
        return this.input;
    }

}
