import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import BlockChain.BlockChainSignedUtility;

import java.io.FileWriter;
import java.security.*;

public class KeysGenerator {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator rsa;

        try (var publicKeyWriter = new FileWriter("public.key");
             var privateKeyWriter = new FileWriter("private.key")) {
            rsa = KeyPairGenerator.getInstance(BlockChainSignedUtility.KEY_ALGORITHM, "BC");
            rsa.initialize(1024,new SecureRandom());
            var keyPair = rsa.generateKeyPair();

            var privateKey = keyPair.getPrivate();
            var publicKey = keyPair.getPublic();

            privateKeyWriter.write(new String(Hex.encode(privateKey.getEncoded())));
            publicKeyWriter.write(new String(Hex.encode(publicKey.getEncoded())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
