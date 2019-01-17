import java.security.PublicKey;

public class TransactionOutput {

    public String hash;
    public PublicKey to;        // Новый владелец монет
    public float value;         // Количество своих монет
    public String parentHash;   // Хеш транзакции, которая создала эту транзакцию

    // Конструктор класса TransactionOutput
    public TransactionOutput(PublicKey to, float value, String parentHash) {
        this.to = to;
        this.value = value;
        this.parentHash = parentHash;
        this.hash = Algorithms.SHA256(Algorithms.keyToString(to)+Float.toString(value) + parentHash);
    }

    // Проверка, что монета пренадлежит этому кошельку
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == to);
    }
}
