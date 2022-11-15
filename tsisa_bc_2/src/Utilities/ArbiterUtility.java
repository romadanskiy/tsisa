package Utilities;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

import static Utilities.CommonUtility.ConcatArrays;
import static org.bouncycastle.util.encoders.Hex.decode;

public class ArbiterUtility {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static PublicKey ConvertStringToPublicKey(String hash) throws Exception{
        var pubKeySpec = new X509EncodedKeySpec(decode(hash));
        var keyFactory = KeyFactory.getInstance(BlockChainSignedWithArbiterUtility.KEY_ALGORITHM);

        return keyFactory.generatePublic(pubKeySpec);
    }

    public static boolean VerifyArbiterSignature(PublicKey publicKey, byte[] input, String signTimestamp, byte[] encSignature) throws Exception {
        var signature = Signature.getInstance(BlockChainSignedWithArbiterUtility.SIGN_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(ConcatArrays(signTimestamp.getBytes(StandardCharsets.UTF_8), input));

        return signature.verify(encSignature);
    }
}
