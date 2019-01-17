package club.plus1;

import java.security.PublicKey;

public class TransactionOutput {

    public String hash;
    public PublicKey to;
    public float value;
    public String parentHash;

    public TransactionOutput(PublicKey to, float value, String parentHash) {
        this.to = to;
        this.value = value;
        this.parentHash = parentHash;
        this.hash = Algoritms.SHA256(Algoritms.keyToString(to)+Float.toString(value) + parentHash);
    }

    public boolean isMine(PublicKey publicKey) {
        return (publicKey == to);
    }
}
