package club.plus1;

import com.google.gson.GsonBuilder;

import java.util.Date;

public class Block {

    public String hash;
    public String prevHash;

    private String data; // будет храниться текстовое сообщение
    private long timeStapm; // количество милисекунд с 01.01.1970

    // Конструктор класса Block
    public Block(String data, String prevHash) {
        this.data = data;
        this.prevHash = prevHash;
        this.timeStapm = new Date().getTime();
        this.hash = calculateHash();
    }

    // Подсчет хеша с помощью SHA-256
    public String calculateHash() {
        return Security.SHA256(prevHash + Long.toString(timeStapm) + data);
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
