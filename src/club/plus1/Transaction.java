package club.plus1;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {

    public PublicKey from;  // Адрес(публичный ключ) отправителя
    public PublicKey to;    // Адрес(публичный ключ) получателя
    public float value;

    public String hash;
    public byte[] sign;     // Подпись нужна, чтобы не могли списать средства без ведома

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;    // Ведётся подсчёт количества сгенерированных транзакций

    // Конструктор класса Transaction
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.inputs = inputs;
    }

    // Подсчет хеша транзакции с помощью SHA-256
    private String calculateHash() {
        sequence++;
        return Algorithms.SHA256(Algorithms.keyToString(from)
                + Algorithms.keyToString(to)
                + Float.toString(value)
                + sequence);
    }

    // Получение подписи приватным ключом
    public void generateSign(PrivateKey privateKey){
        String data = Algorithms.keyToString(from) + Algorithms.keyToString(to) + Float.toString(value);
        sign = Algorithms.applySign(privateKey, data);
    }

    // Проверка подписи публичным ключом отправителя
    public boolean verifySign(){
        String data = Algorithms.keyToString(from) + Algorithms.keyToString(to) + Float.toString(value);
        return Algorithms.verifySign(from, data, sign);
    }

    // Возвращает "истина" если можно создать транзакцию
    public boolean processTransaction(){
        if(verifySign() == false){
            System.out.println("#Не удалось проверить подпись");
            return false;
        }

        // Сбор входов транзакции, с проверкой, что они не потрачены
        for (TransactionInput i : inputs) {
            i.output = Chain.unspentOutputs.get(i.outputHash);
        }

        // Проверка размера транзакции
        if(getInputsValue() < Chain.minimumTransaction){
            System.out.println("#Недостаточно средств на входе транзакции: " + getInputsValue());
            return false;
        }

        // Созание выходов транзакции
        float leftOver = getInputsValue() - value;
        hash = calculateHash();
        outputs.add(new TransactionOutput(this.to, value, hash));
        outputs.add(new TransactionOutput(this.from, leftOver, hash));

        // Выходы транзакции добавляются в непотраченные
        for(TransactionOutput o : outputs){
            Chain.unspentOutputs.put(o.hash, o);
        }

        // Выходы, связанные с входами транзакции удаляются из списка не потраченного, т.к. они потрачены
        for(TransactionInput i : inputs){
            if(i.output == null) continue;
            Chain.unspentOutputs.remove(i.output.hash);
        }

        return true;
    }

    // Подсчет суммы всех входов
    public float getInputsValue(){
        float total = 0;
        for (TransactionInput i : inputs) {
            if(i.output == null) continue; // Если транзакция не найдена, то пропускаем её
            total += i.output.value;
        }
        return total;
    }

    // Подсчёт суммы всех выходов
    public float getOutputsValue(){
        float total = 0;
        for (TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }
}
