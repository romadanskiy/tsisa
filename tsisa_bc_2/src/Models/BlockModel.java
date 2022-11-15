package Models;

public class BlockModel {
    private int blockNumber;
    private BlockDataModel data;
    private byte[] prevHash;
    private byte[] dataSign;
    private byte[] arbiterSign;
    private String arbiterSignTimestamp;

    public BlockModel(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int GetBlockNumber() {
        return blockNumber;
    }

    public void SetBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public BlockDataModel GetData() {
        return data;
    }

    public String GetStringData() {
        return data.toString();
    }

    public void SetData(BlockDataModel data) {
        this.data = data;
    }

    public byte[] GetPrevHash() {
        return prevHash;
    }

    public void SetPrevHash(byte[] prevHash) {
        this.prevHash = prevHash;
    }

    public byte[] GetArbiterSign() {
        return arbiterSign;
    }

    public void SetArbiterSign(byte[] arbiterSign) {
        this.arbiterSign = arbiterSign;
    }

    public byte[] GetDataSign() {
        return dataSign;
    }

    public void SetDataSign(byte[] sign) {
        this.dataSign = sign;
    }

    public String GetArbiterSignTimestamp() {
        return arbiterSignTimestamp;
    }

    public void SetArbiterSignTimestamp(String arbiterSignTimestamp) {
        this.arbiterSignTimestamp = arbiterSignTimestamp;
    }
}
