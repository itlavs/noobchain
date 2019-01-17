package club.plus1;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Base64;

public class Algorithms {

    // Хеширование строки по алгоритму SHA256
    public static String SHA256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // Генерация пары приватного и публичного ключа
    public static KeyPair generateKeyPair(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
            return keyGen.generateKeyPair();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // Получение ключа в виде текста
    public static String keyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Подписание строки приватным ключом по алгоритму ECDSA
    public static byte[] applySign(PrivateKey privateKey, String input){
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSign = dsa.sign();
            output = realSign;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    // Проверка подписи по алгоритму ECDSA
    public static boolean verifySign(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Добавляет транзакции в массив и возващает корень дерева Меркла
    public static String getRoot(ArrayList<Transaction> transactions){
        int count = transactions.size();
        ArrayList<String> prevLayer = new ArrayList<String>();
        for(Transaction transaction : transactions){
            prevLayer.add(transaction.hash);
        }
        ArrayList<String> treeLayer = prevLayer;
        while (count > 1){
            treeLayer = new ArrayList<String>();
            for (int i = 1; i < prevLayer.size(); i++){
                treeLayer.add(SHA256(prevLayer.get(i-1) + prevLayer.get(i)));
            }
            count = treeLayer.size();
            prevLayer = treeLayer;
        }
        return (treeLayer.size() == 1) ? treeLayer.get(0) : "";
    }
}
