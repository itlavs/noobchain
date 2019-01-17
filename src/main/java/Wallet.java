import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;
    public HashMap<String,TransactionOutput> unspentOutputs = new HashMap<String,TransactionOutput>();

    // Конструктор класса Wallet
    public Wallet() {
        KeyPair keyPair = Algorithms.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    // Заполнение непотраченных транзакций и рассчет баланса кошелька
    public float getBalance() {
        float total = 0;

        for(Map.Entry<String, TransactionOutput> item : Chain.unspentOutputs.entrySet()){
            TransactionOutput unspent = item.getValue();
            if(unspent.isMine(publicKey)){
                unspentOutputs.put(unspent.hash, unspent);
                total += unspent.value;
            }
        }
        return total;
    }

    // Создание новой исходящей из кошелька транзакции
    public Transaction sendFunds(PublicKey _to, float value){
        if(getBalance() < value){
            System.out.println("#Недостаточно средств для отправки. Транзакция отменена.");
            return null;
        }

        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: unspentOutputs.entrySet()){
            TransactionOutput unspent = item.getValue();
            total += unspent.value;
            inputs.add(new TransactionInput(unspent.hash));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _to, value, inputs);
        newTransaction.generateSign(privateKey);

        for(TransactionInput input: inputs){
            unspentOutputs.remove(input.outputHash);
        }
        return newTransaction;
    }
}
