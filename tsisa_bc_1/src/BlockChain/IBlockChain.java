package BlockChain;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface IBlockChain {
    void MakeBlockChain();

    void MakeDamage(int blockNumber);

    void Print() throws NoSuchAlgorithmException, NoSuchProviderException;

    boolean VerifyBlock(int blockNumber) throws GeneralSecurityException;

    boolean Verify() throws GeneralSecurityException;
}
