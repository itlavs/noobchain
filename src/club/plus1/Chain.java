package club.plus1;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Chain {

    private ArrayList<Block> chain = new ArrayList<Block>();

    // Добавляем блок в блокчейн и возвращаем хеш добавленного блока
    public String add(String text, String hash){
        chain.add(new Block(text, hash));
        return last().hash;
    }

    // Получение элемента цепочки по индексу
    public Block get(int number){
        return chain.get(number);
    }

    // Получение длины цепочки
    public int size(){
        return chain.size();
    }

    // Получение послденего блока цепочки
    public Block last(){
        return get(size()-1);
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(chain);
    }
}
