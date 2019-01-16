package club.plus1;

import com.google.gson.GsonBuilder;
import java.util.Date;

public class Block {

    public String hash;
    public String prevHash;

    private String data;    // будет храниться текстовое сообщение
    private long timeStamp; // количество милисекунд с 01.01.1970
    private int nonce;      // основание для рассчёта хеша

    // Конструктор класса Block
    public Block(String data, String prevHash) {
        this.data = data;
        this.prevHash = prevHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    // Подсчет хеша с помощью SHA-256
    public String calculateHash() {
        return Security.SHA256(prevHash + Long.toString(timeStamp) + Integer.toString(nonce) + data);
    }

    // Майнинг
    public String mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)){
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Блок найден! : " + hash);
        return hash;
    }

    // Получение блока в виде текста в формате Json
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
