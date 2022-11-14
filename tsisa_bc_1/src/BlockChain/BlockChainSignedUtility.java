package BlockChain;

import Models.Block;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class BlockChainSignedUtility {
    public static final String DIGEST_ALGORITHM = "SHA-256";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHM = "SHA256withRSAandMGF1";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] GetHash(Block block) throws NoSuchAlgorithmException {
        var builder = new StringBuilder();
        for (var s : block.GetData()) {
            builder.append(s);
        }

        var digest = MessageDigest.getInstance(DIGEST_ALGORITHM);

        return digest.digest(Concat(block.GetPrevHash(), builder.toString().getBytes(StandardCharsets.UTF_8)));
    }

    private static byte[] Concat(byte[] a, byte[] b) {
        if (a == null)
            return b;

        if (b == null)
            return a;

        var lengthA = a.length;
        var lengthB = b.length;
        var c = new byte[lengthA + lengthB];
        System.arraycopy(a, 0, c, 0, lengthA);
        System.arraycopy(b, 0, c, lengthA, lengthB);

        return c;
    }

    public static KeyPair LoadKeys() throws Exception {
        var publicKeyHex = Files.readAllBytes(Paths.get("public.key"));
        var privateKeyHex = Files.readAllBytes(Paths.get("private.key"));

        var publicKey = ConvertArrayToPublicKey(Hex.decode(publicKeyHex));
        var privateKey = ConvertArrayToPrivateKey(Hex.decode(privateKeyHex));

        return new KeyPair(publicKey, privateKey);
    }

    private static PublicKey ConvertArrayToPublicKey(byte[] encoded) throws Exception {
        var pubKeySpec = new X509EncodedKeySpec(encoded);
        var keyFactory = KeyFactory.getInstance(BlockChainSignedUtility.KEY_ALGORITHM);

        return keyFactory.generatePublic(pubKeySpec);
    }

    private static PrivateKey ConvertArrayToPrivateKey(byte[] encoded) throws Exception {
        var keySpec = new PKCS8EncodedKeySpec(encoded);
        var keyFactory = KeyFactory.getInstance(BlockChainSignedUtility.KEY_ALGORITHM);

        return keyFactory.generatePrivate(keySpec);
    }

    public static byte[] GenerateSignature(PrivateKey privateKey, byte[] input) throws GeneralSecurityException {
        var signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(input);

        return signature.sign();
    }

    public static boolean VerifySignature(PublicKey publicKey, byte[] input, byte[] encSignature) throws GeneralSecurityException {
        var signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(input);

        return signature.verify(encSignature);
    }
}
