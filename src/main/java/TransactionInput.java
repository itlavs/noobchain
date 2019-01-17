public class TransactionInput {

    public String outputHash;           // Хеш связанной выходной транзакции
    public TransactionOutput output;    // Весь не распределённый выход

    // Конструктор класса TransactionInput
    public TransactionInput(String outputHash) {
        this.outputHash = outputHash;
    }
}
