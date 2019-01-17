package club.plus1;

import java.util.ArrayList;
import java.util.HashMap;

public class Chain {

    private static ArrayList<Block> chain = new ArrayList<Block>();
    public static HashMap<String,TransactionOutput> unspentOutputs = new HashMap<String,TransactionOutput>();
    public static int difficulty = 5;
    public static float minimumTransaction = 0.1f;
    public static Transaction mainTransaction;

    // Превращаем класс в одиночку(Singleton) с помощью On Demand Holder
    public static class ChainHolder {
        public static final Chain HOLDER_INSTANCE = new Chain();
    }
    public static Chain get() {
        return ChainHolder.HOLDER_INSTANCE;
    }

    public static void addBlock(Block newBlock){
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }

    // Проверка блокчейна на валидность
    public static Boolean isValid() {
        Block thisBlock;
        Block prevBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUnspend = new HashMap<String, TransactionOutput>();
        tempUnspend.put(mainTransaction.outputs.get(0).hash, mainTransaction.outputs.get(0));

        for(int i = 1; i < chain.size(); i++){
            thisBlock = chain.get(i);
            prevBlock = chain.get(i - 1);
            // Проверка текущего блока
            if(!thisBlock.hash.equals(thisBlock.calculateHash())){
                System.out.println("#Текущий хеш некорректен");
                return false;
            }
            // Проверка предыдущего блока
            if(!prevBlock.hash.equals(thisBlock.prevHash)){
                System.out.println("#Предыдущий хеш некорректен");
                return false;
            }
            // Проверка корректности майнинга
            if(!thisBlock.hash.substring(0, difficulty).equals(hashTarget)){
                System.out.println("#Корректный блок не найден");
                return false;
            }

            TransactionOutput tempOutput;
            for(int t = 0; t < thisBlock.transactions.size(); t++){
                Transaction thisTransaction = thisBlock.transactions.get(t);
                if(!thisTransaction.verifySign()){
                    System.out.println("#Подпись в транзакции(" + t + ") не верна.");
                    return false;
                }
                if(thisTransaction.getInputsValue() != thisTransaction.getOutputsValue()){
                    System.out.println("#Входы не равны выходам в транзакции(" + t + ")");
                    return false;
                }

                for(TransactionInput input: thisTransaction.inputs){
                    tempOutput = tempUnspend.get(input.outputHash);
                    if(tempOutput == null){
                        System.out.println("#Выход транзакции(" + t + ") не связан с входом");
                        return false;
                    }
                    if(input.output.value != tempOutput.value){
                        System.out.println("#Выход транзакции(\" + t + \") связан с вхдом с некорректным значением");
                        return false;
                    }
                    tempUnspend.remove(input.outputHash);
                }
                for(TransactionOutput output: thisTransaction.outputs){
                    tempUnspend.put(output.hash, output);
                }
                if(thisTransaction.outputs.get(0).to != thisTransaction.to){
                    System.out.println("#В транзакции(" + t + ") не известно, кто является получателем");
                    return false;
                }
                if(thisTransaction.outputs.get(1).to != thisTransaction.from){
                    System.out.println("#В транзакции(" + t + ") изменен получатель");
                    return false;
                }
            }
        }
        System.out.println("Блокчейн корректен.");
        return true;
    }
}
