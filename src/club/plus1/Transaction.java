package club.plus1;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {

    public PublicKey from;  // Адрес(публичный ключ) отправителя
    public PublicKey to;    // Адрес(публичный ключ) получателя
    public float value;

    public String hash;
    public byte[] sign;     // Подпись нужна, чтобы не могли списать средства без ведома

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;    // Ведётся подсчёт количества сгенерированных транзакций

    // Конструктор класса Transaction
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.inputs = inputs;
    }

    // Подсчет хеша транзакции с помощью SHA-256
    private String calculateHash() {
        sequence++;
        return SecurityUtils.SHA256(SecurityUtils.keyToString(from)
                + SecurityUtils.keyToString(to)
                + Float.toString(value)
                + sequence);
    }

    // Получение подписи приватным ключом
    public void generateSign(PrivateKey privateKey){
        String data = SecurityUtils.keyToString(from) + SecurityUtils.keyToString(to) + Float.toString(value);
        sign = SecurityUtils.applySign(privateKey, data);
    }

    // Проверка подписи публичным ключом отправителя
    public boolean verifySign(){
        String data = SecurityUtils.keyToString(from) + SecurityUtils.keyToString(to) + Float.toString(value);
        return SecurityUtils.verifySign(from, data, sign);
    }

}
