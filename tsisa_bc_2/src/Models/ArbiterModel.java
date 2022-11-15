package Models;

public class ArbiterModel {
    private String timestamp;
    private byte[] signature;

    public ArbiterModel(String timestamp, byte[] signature) {
        this.timestamp = timestamp;
        this.signature = signature;
    }

    public String GetTimestamp() {
        return timestamp;
    }

    public void SetTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] GetSignature() {
        return signature;
    }

    public void SetSignature(byte[] signature) {
        this.signature = signature;
    }
}
