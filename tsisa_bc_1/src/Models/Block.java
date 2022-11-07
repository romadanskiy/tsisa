package Models;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private int blockNumber;
    private List<String> data = new ArrayList<>();
    private byte[] prevHash;
    private byte[] sign;

    public Block(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int GetBlockNumber() {
        return blockNumber;
    }

    public void SetBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public List<String> GetData() {
        return data;
    }

    public void SetData(List<String> data) {
        this.data = data;
    }

    public byte[] GetPrevHash() {
        return prevHash;
    }

    public void SetPrevHash(byte[] prevHash) {
        this.prevHash = prevHash;
    }

    public byte[] GetSign() {
        return sign;
    }

    public void SetSign(byte[] sign) {
        this.sign = sign;
    }
}
