package BlockChain;

import Models.BlockModel;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface IBlockChain {
    void MakeBlockChain() throws Exception;

    void AddBlock(BlockModel block);

    void MakeDamage(int blockNumber) throws IOException;

    void Print() throws NoSuchAlgorithmException, NoSuchProviderException;

    boolean VerifyBlock(int blockNumber) throws Exception;

    boolean Verify() throws Exception;
}
