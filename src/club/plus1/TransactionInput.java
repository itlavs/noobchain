package club.plus1;

public class TransactionInput {

    public String outputHash;
    public TransactionOutput output;

    public TransactionInput(String outputHash) {
        this.outputHash = outputHash;
    }
}
