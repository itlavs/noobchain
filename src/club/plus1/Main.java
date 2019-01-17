package club.plus1;

import java.security.Security;

public class Main {

    public static void main(String[] args) {

        // подготовка блокчейна, кошельков и провайдера ключей
        String hash = "0";
        Chain blockchain = Chain.get();
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();

        // Создание кошельков
        Wallet coinbase = new Wallet();

        // Подготовка главного блока, с которого и будут переводиться деньги
        Chain.mainTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        Chain.mainTransaction.generateSign(coinbase.privateKey);
        Chain.mainTransaction.hash = "0";
        Chain.mainTransaction.outputs.add(new TransactionOutput(Chain.mainTransaction.to, Chain.mainTransaction.value, Chain.mainTransaction.hash));
        Chain.unspentOutputs.put(Chain.mainTransaction.outputs.get(0).hash, Chain.mainTransaction.outputs.get(0));

        System.out.println("Создание и майнинг главного блока...");
        Block mainBlock = new Block("0");
        mainBlock.addTransaction(Chain.mainTransaction);
        Chain.addBlock(mainBlock);

        // Тестирование блокчейна
        Block block1 = new Block(mainBlock.hash);
        System.out.println("\nБаланс кошелька №1: " + walletA.getBalance());
        System.out.println("Баланс кошелька №2: " + walletB.getBalance());

        System.out.println("\nОтправляем 40 монет с кошелька №1 на кошелек №2");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        Chain.addBlock(block1);
        System.out.println("Баланс кошелька №1: " + walletA.getBalance());
        System.out.println("Баланс кошелька №2: " + walletB.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nОтправляем больше чем имеем, 1000 монет с кошелька №1 на кошелек №2");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        Chain.addBlock(block2);
        System.out.println("Баланс кошелька №1: " + walletA.getBalance());
        System.out.println("Баланс кошелька №2: " + walletB.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nОтправляем 20 монет с кошелька №2 на кошелек №1");
        block1.addTransaction(walletB.sendFunds(walletA.publicKey, 20f));
        Chain.addBlock(block3);
        System.out.println("Баланс кошелька №1: " + walletA.getBalance());
        System.out.println("Баланс кошелька №2: " + walletB.getBalance());

        blockchain.isValid();
    }
}
