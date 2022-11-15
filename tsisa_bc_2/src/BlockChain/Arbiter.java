package BlockChain;

import Models.ArbiterModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.nio.charset.Charset;

public class Arbiter {
    private final OkHttpClient httpClient = new OkHttpClient();
    private static final String URL_PUBLIC = "http://89.108.115.118/ts/public";
    private static final String URL_DIGEST = "http://89.108.115.118/ts?digest=";

    public String GetPublicKey() throws Exception {
        var request = new Request.Builder().url(URL_PUBLIC).build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new Exception(response.toString());

            var body = response.body();

            return body == null ? "" : body.string();
        }
    }

    public ArbiterModel GetArbiter(byte[] blockHash) throws Exception {
        var url = URL_DIGEST + new String(Hex.encode(blockHash));
        var request = new Request.Builder().url(url).build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            var body = response.body();

            if (body == null)
                return null;

            var objectMapper = new ObjectMapper();
            var objectNode = objectMapper.readTree(body.string());
            var timestamp = objectNode.get("timeStampToken").get("ts").toString().replace("\"", "");
            var signature = Hex.decode(objectNode.get("timeStampToken").get("signature").toString().replace("\"", "").getBytes(Charset.defaultCharset()));

            return new ArbiterModel(timestamp, signature);
        }
    }
}
