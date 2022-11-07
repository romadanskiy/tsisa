import BlockChain.BlockChainSigned;
import BlockChain.BlockChainSignedUtility;

import java.security.GeneralSecurityException;

public class Main {
    public static void main(String[] args) {
        try {
            var keyPair = BlockChainSignedUtility.LoadKeys();
            var blockChainSigned = new BlockChainSigned(keyPair, 7);
            var damagedBlockNumber = 3;

            blockChainSigned.MakeBlockChain();
            PrintBlockChainInfo(blockChainSigned);

            System.out.println("\n-------------------------------");
            System.out.println("!!! Damaged block number: " + damagedBlockNumber + " !!!");
            System.out.println("-------------------------------\n");

            blockChainSigned.MakeDamage(damagedBlockNumber);
            PrintBlockChainInfo(blockChainSigned);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintBlockChainInfo(BlockChainSigned blockChainSigned) throws GeneralSecurityException {
        blockChainSigned.Print();
        System.out.println("Verify all: " + blockChainSigned.Verify());
        for (var i=0; i<blockChainSigned.GetBlockChainLength(); i++) {
            System.out.println("Verify block #" + i + ": " + blockChainSigned.VerifyBlock(i));
        }
    }
}