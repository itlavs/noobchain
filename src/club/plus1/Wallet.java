package club.plus1;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;

    // Конструктор класса Wallet
    public Wallet() {
        KeyPair keyPair = SecurityUtils.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

}
