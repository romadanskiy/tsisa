import org.bouncycastle.util.encoders.Hex;
import BlockChain.BlockChainSignedUtility;

import java.io.FileWriter;
import java.security.*;

public class KeysGenerator {
    public static void main(String[] args) {
        try (var publicKeyWriter = new FileWriter("public.key");
             var privateKeyWriter = new FileWriter("private.key")) {

            // генерируем пару асимметричных ключей (открытый + закрытый)
            // закрытый ключ используется для шифрования данных
            // открытый - для расшифровки
            var rsaKeyPairGenerator = KeyPairGenerator.getInstance(BlockChainSignedUtility.KEY_ALGORITHM);
            rsaKeyPairGenerator.initialize(1024, new SecureRandom());

            var keyPair = rsaKeyPairGenerator.generateKeyPair();
            var privateKey = keyPair.getPrivate();
            var publicKey = keyPair.getPublic();

            privateKeyWriter.write(new String(Hex.encode(privateKey.getEncoded())));
            publicKeyWriter.write(new String(Hex.encode(publicKey.getEncoded())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
