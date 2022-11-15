package Utilities;

import Models.BlockModel;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static Utilities.CommonUtility.ConcatArrays;

public class BlockChainSignedWithArbiterUtility {
    public static final String DIGEST_ALGORITHM = "SHA-256";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHM = "SHA256withRSA";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] GenerateSignature(PrivateKey privateKey, byte[] data) throws GeneralSecurityException {
        var signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);

        return signature.sign();
    }

    public static byte[] GetHash(BlockModel block) throws NoSuchAlgorithmException {
        byte[] data = block.GetStringData().getBytes(StandardCharsets.UTF_8);
        byte[] prevHashAndData = ConcatArrays(block.GetPrevHash(), data);
        byte[] dataToDigest = ConcatArrays(prevHashAndData, block.GetDataSign());

        var digest = MessageDigest.getInstance(DIGEST_ALGORITHM);

        return digest.digest(dataToDigest);
    }

    public static KeyPair LoadKeys() throws Exception {
        var publicKeyHex = Files.readAllBytes(Paths.get("src/Resources/public.key"));
        var privateKeyHex = Files.readAllBytes(Paths.get("src/Resources/private.key"));

        var publicKey = ConvertArrayToPublicKey(Hex.decode(publicKeyHex));
        var privateKey = ConvertArrayToPrivateKey(Hex.decode(privateKeyHex));

        return new KeyPair(publicKey, privateKey);
    }

    private static PublicKey ConvertArrayToPublicKey(byte[] encoded) throws Exception {
        var pubKeySpec = new X509EncodedKeySpec(encoded);
        var keyFactory = KeyFactory.getInstance(BlockChainSignedWithArbiterUtility.KEY_ALGORITHM);

        return keyFactory.generatePublic(pubKeySpec);
    }

    private static PrivateKey ConvertArrayToPrivateKey(byte[] encoded) throws Exception {
        var keySpec = new PKCS8EncodedKeySpec(encoded);
        var keyFactory = KeyFactory.getInstance(BlockChainSignedWithArbiterUtility.KEY_ALGORITHM);

        return keyFactory.generatePrivate(keySpec);
    }

    public static boolean VerifyDataSignature(PublicKey publicKey, byte[] input, byte[] encSignature) throws GeneralSecurityException {
        var signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(input);

        return signature.verify(encSignature);
    }
}
