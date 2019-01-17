import java.util.ArrayList;
import java.util.Date;

public class Block {

    public String hash;
    public String prevHash;

    public String root;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    private long timeStamp; // количество милисекунд с 01.01.1970
    private int nonce;      // основание для рассчёта хеша

    // Конструктор класса Block
    public Block(String prevHash) {
        this.prevHash = prevHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    // Подсчет хеша блока с помощью SHA-256
    public String calculateHash() {
        return Algorithms.SHA256(prevHash + Long.toString(timeStamp) + Integer.toString(nonce) + root);
    }

    // Майнинг
    public String mineBlock(int difficulty) {
        root = Algorithms.getRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)){
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Блок найден! : " + hash);
        return hash;
    }

    // Добавление транзакции в блок
    public boolean addTransaction(Transaction transaction){
        if(transaction == null) return false;
        if(prevHash != "0"){
            if(transaction.processTransaction() != true){
                System.out.println("Транзакция завершилась неуспешно. Отменена.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Транзакция успешно добавлена в блок");
        return true;
    }
}
