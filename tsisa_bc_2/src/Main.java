import BlockChain.Arbiter;
import BlockChain.BlockChainSignedWithArbiter;
import Services.BlockService;
import Utilities.ArbiterUtility;
import Utilities.BlockChainSignedWithArbiterUtility;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;

public class Main {
    private static final Arbiter arbiter = new Arbiter();

    public static void main(String[] args) {
        try {
            var keyPair = BlockChainSignedWithArbiterUtility.LoadKeys();
            var arbiterKey = ArbiterUtility.ConvertStringToPublicKey(arbiter.GetPublicKey());
            var blockChainLength = 7;

            var damagedBlockNumber = 3;

            var blockChain = GenerateBlockChain(keyPair, arbiterKey, blockChainLength);
            PrintBlockChainInfo(blockChain);

            System.out.println("\n-------------------------------");
            System.out.println("!!! Damaged block number: " + damagedBlockNumber + " !!!");
            System.out.println("-------------------------------\n");

            blockChain = GetBlockChainFromFiles(keyPair, arbiterKey, blockChainLength);
            blockChain.MakeDamage(damagedBlockNumber);
            PrintBlockChainInfo(blockChain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintBlockChainInfo(BlockChainSignedWithArbiter blockChainSigned) throws Exception {
        blockChainSigned.Print();
        System.out.println("Verify all: " + blockChainSigned.Verify());
        for (var i = 0; i < blockChainSigned.GetBlockChainLength(); i++) {
            System.out.println("Verify block #" + i + ": " + blockChainSigned.VerifyBlock(i));
        }
    }

    private static BlockChainSignedWithArbiter GenerateBlockChain(KeyPair keyPair, PublicKey arbiterKey, int blockChainLength) throws Exception {
        var blockChain = new BlockChainSignedWithArbiter(keyPair, arbiterKey, blockChainLength);
        blockChain.MakeBlockChain();

        return blockChain;
    }

    private static BlockChainSignedWithArbiter GetBlockChainFromFiles(KeyPair keyPair, PublicKey arbiterKey, int blockChainLength) throws IOException {
        var blockChain = new BlockChainSignedWithArbiter(keyPair, arbiterKey, blockChainLength);

        for (var i = 0; i < blockChainLength; i++) {
            var block = BlockService.ReadBlockFromFile(i);
            blockChain.AddBlock(block);
        }

        return blockChain;
    }
}