package club.plus1;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Подготовка консоли и текущего хеша
        System.out.println("Для добавления блока напишите любой текст и нажмите Enter, либо нажмите Enter в пустой строке для выхода:");
        Scanner in = new Scanner(System.in);
        String text;
        String hash = "0";

        // Добавляем блоки по каждой введённой строке и тут же выводим созданный объект
        do {
            text = in.nextLine();
            if (!text.equals("")) {
                Block block = new Block(text, hash);
                hash = block.hash;
                System.out.println("Добавлен блок:\n" + block.toString());
            }
        }
        while (!text.equals(""));
    }
}
