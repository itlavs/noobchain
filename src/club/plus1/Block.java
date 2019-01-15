package club.plus1;

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

    public String calculateHash() {
        return Security.SHA256(prevHash + Long.toString(timeStapm) + data);
    }

    // Временное решение для удобства тестирования
    @Override
    public String toString() {
        return "Prev: " + this.prevHash + "\n" + "Hash: " + this.hash + "\n"
                + "Time: " + String.format("%d", this.timeStapm) + "\n"
                + "Data: " + this.data;
    }
}
