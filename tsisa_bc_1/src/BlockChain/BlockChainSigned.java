package BlockChain;

import Models.Block;
import org.bouncycastle.util.encoders.Hex;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BlockChainSigned implements IBlockChain {
    private static final List<Block> blockChain = new ArrayList<>();
    private final KeyPair keyPair;
    private final int blockChainLength;

    public BlockChainSigned(KeyPair keyPair, int blockChainLength) {
        this.keyPair = keyPair;
        this.blockChainLength = blockChainLength;
    }

    public int GetBlockChainLength() {
        return blockChainLength;
    }

    @Override
    public void MakeBlockChain() {
        byte[] prevHash = null;

        for (var i = 0; i < blockChainLength; i++) {
            var block = new Block(i);
            block.GetData().add("\"data\": \"Data " + i + "\"");
            block.GetData().add("\"timestamp\": \"" + new Date() + "\"");
            block.SetPrevHash(prevHash);

            try {
                prevHash = BlockChainSignedUtility.GetHash(block);
                block.SetSign(BlockChainSignedUtility.GenerateSignature(keyPair.getPrivate(), prevHash));
            } catch (Exception e) {
                e.printStackTrace();
            }

            blockChain.add(block);
        }
    }

    @Override
    public void MakeDamage(int blockNumber) {
        blockChain.get(blockNumber).GetData().remove(0);
        blockChain.get(blockNumber).GetData().add(0, "\"data\": \"What is it?!\"");
    }

    @Override
    public void Print() throws NoSuchAlgorithmException, NoSuchProviderException {
        for (int i = 0; i < blockChainLength; i++) {
            var block = blockChain.get(i);
            System.out.println("~~~~~~~~~ " + block.GetBlockNumber() + " ~~~~~~~~~");
            System.out.println("prev hash: " + (block.GetPrevHash() != null ? new String(Hex.encode(block.GetPrevHash())) : ""));
            for (String s : block.GetData()) System.out.println(s);
            System.out.println("digest: " + new String(Hex.encode(BlockChainSignedUtility.GetHash(block))));
            System.out.println("signature: " + new String(Hex.encode(block.GetSign())));
            System.out.println();
        }
    }

    @Override
    public boolean VerifyBlock(int blockNumber) throws GeneralSecurityException {
        byte[] prevHash;

        if (blockNumber > 0) {
            prevHash = BlockChainSignedUtility.GetHash(blockChain.get(blockNumber - 1));

            if (!Arrays.equals(prevHash, blockChain.get(blockNumber).GetPrevHash())) {
                return false;
            }
        }

        prevHash = BlockChainSignedUtility.GetHash(blockChain.get(blockNumber));

        return BlockChainSignedUtility.VerifySignature(keyPair.getPublic(), prevHash, blockChain.get(blockNumber).GetSign());
    }

    @Override
    public boolean Verify() throws GeneralSecurityException {
        var prevHash = BlockChainSignedUtility.GetHash(blockChain.get(0));
        for (var i = 1; i < blockChainLength; i++) {
            if (!Arrays.equals(prevHash, blockChain.get(i).GetPrevHash())) {
                return false;
            }

            prevHash = BlockChainSignedUtility.GetHash(blockChain.get(i));

            if (!BlockChainSignedUtility.VerifySignature(keyPair.getPublic(), prevHash, blockChain.get(i).GetSign())) {
                return false;
            }
        }

        return true;
    }
}
