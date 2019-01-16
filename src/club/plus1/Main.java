package club.plus1;

import java.security.Security;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Тестирование кошельков
        Wallet walletA;
        Wallet walletB;

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Создание кошельков
        walletA = new Wallet();
        walletB = new Wallet();
        //Тестируем приватный и публичный ключи
        System.out.println("Приватный: " + SecurityUtils.keyToString(walletA.privateKey));
        System.out.println("Публичный: " + SecurityUtils.keyToString(walletA.publicKey));
        //Тестовая транзакция между кошельками WalletA и walletB
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSign(walletA.privateKey);
        //Верификация подписи
        if(transaction.verifySign()){
            System.out.println("Подпись верифицирована");
        } else {
            System.out.println("Подпись НЕ верифицирована!");
        }


        /*************** Тестирование блокчейна

        // Подготовка консоли
        System.out.println("Для добавления блока напишите любой текст и нажмите Enter, либо нажмите Enter в пустой строке для выхода:");
        Scanner in = new Scanner(System.in);
        String text;

        // подготовка блокчейна
        String hash = "0";
        Chain blockchain = Chain.get();

        // Добавляем блоки по каждой введённой строке и тут же выводим созданный объект
        do {
            text = in.nextLine();
            if (!text.equals("")) {
                hash = blockchain.add(text, hash);
                System.out.println("Добавлен блок:");
                System.out.println(blockchain.last().toString());
            }
        }
        while (!text.equals(""));

        if(blockchain.isValid()){
            System.out.println("Блокчейн корректен: ");
        } else {
            System.out.println("Блокчейн некорректен: ");
        }
        System.out.println(blockchain.toString());
         */
    }
}
