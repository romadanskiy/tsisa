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

    public static byte[] GetHash(Block block) throws NoSuchAlgorithmException, NoSuchProviderException {
        var builder = new StringBuilder();
        for (var s : block.GetData()) {
            builder.append(s);
        }

        var digest = MessageDigest.getInstance(DIGEST_ALGORITHM,"BC");

        return digest.digest(Concat(block.GetPrevHash(), builder.toString().getBytes(StandardCharsets.UTF_8)));
    }

    public static byte[] Concat(byte[] a, byte[] b) {
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

        PublicKey publicKey = ConvertArrayToPublicKey(Hex.decode(publicKeyHex), KEY_ALGORITHM);
        PrivateKey privateKey = ConvertArrayToPrivateKey(Hex.decode(privateKeyHex), KEY_ALGORITHM);

        return new KeyPair(publicKey, privateKey);
    }

    public static PublicKey ConvertArrayToPublicKey(byte[] encoded, String algorithm) throws Exception {
        var pubKeySpec = new X509EncodedKeySpec(encoded);
        var keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePublic(pubKeySpec);
    }

    public static PrivateKey ConvertArrayToPrivateKey(byte[] encoded, String algorithm) throws Exception {
        var keySpec = new PKCS8EncodedKeySpec(encoded);
        var keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePrivate(keySpec);
    }

    public static byte[] GenerateRSAPSSSignature(PrivateKey privateKey, byte[] input) throws GeneralSecurityException {
        Signature signature = Signature.getInstance(SIGN_ALGORITHM, "BC");
        signature.initSign(privateKey);
        signature.update(input);
        return signature.sign();
    }

    public static boolean VerifyRSAPSSSignature(PublicKey publicKey, byte[] input, byte[] encSignature) throws GeneralSecurityException {
        var signature = Signature.getInstance(SIGN_ALGORITHM, "BC");
        signature.initVerify(publicKey);
        signature.update(input);

        return signature.verify(encSignature);
    }
}
