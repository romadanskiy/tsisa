import Utilities.BlockChainSignedWithArbiterUtility;
import org.bouncycastle.util.encoders.Hex;

import java.io.FileWriter;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

public class KeysGenerator {
    public static void main(String[] args) {
        try (var publicKeyWriter = new FileWriter("src/Resources/public.key");
             var privateKeyWriter = new FileWriter("src/Resources/private.key")) {

            // генерируем пару асимметричных ключей (открытый + закрытый)
            // закрытый ключ используется для шифрования данных
            // открытый - для расшифровки
            var rsaKeyPairGenerator = KeyPairGenerator.getInstance(BlockChainSignedWithArbiterUtility.KEY_ALGORITHM);
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
