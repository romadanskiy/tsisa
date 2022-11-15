package BlockChain;

import Models.BlockDataModel;
import Models.BlockModel;
import Services.BlockService;
import Utilities.ArbiterUtility;
import Utilities.BlockChainSignedWithArbiterUtility;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BlockChainSignedWithArbiter implements IBlockChain {
    private static final List<BlockModel> blockChain = new ArrayList<>();
    private final KeyPair keyPair;
    private final PublicKey arbiterKey;
    private final int blockChainLength;

    private static final Arbiter arbiter = new Arbiter();

    public BlockChainSignedWithArbiter(KeyPair keyPair, PublicKey arbiterKey, int blockChainLength) {
        this.keyPair = keyPair;
        this.arbiterKey = arbiterKey;
        this.blockChainLength = blockChainLength;
    }

    public int GetBlockChainLength() {
        return blockChainLength;
    }

    @Override
    public void MakeBlockChain() throws Exception {
        byte[] prevHash = null;
        for (var i = 0; i < blockChainLength; i++) {
            // создание нового блока
            var block = new BlockModel(i);

            // создание и запись данных текущего блока
            var data = new BlockDataModel("Data " + i, new Date().toString());
            block.SetData(data);

            // запись хеша предыдущего блока
            block.SetPrevHash(prevHash);

            // подписание данных текущего блока
            block.SetDataSign(BlockChainSignedWithArbiterUtility.GenerateSignature(keyPair.getPrivate(), block.GetStringData().getBytes(StandardCharsets.UTF_8)));

            // вычисление хеша текущего блока
            // (от совокупности хеша предыдущего блока, данных и подписи данных)
            var currentHash = BlockChainSignedWithArbiterUtility.GetHash(block);

            // передача хеша текущего блока арбитру для подписи
            // и получение от арбитра метки времени и значения подписи
            var arbiterModel = arbiter.GetArbiter(currentHash);

            // внесение метки времени и значения подписи от арбитра в текущий блок
            block.SetArbiterSignTimestamp(arbiterModel.GetTimestamp());
            block.SetArbiterSign(arbiterModel.GetSignature());

            // добавление текущего блока в блокчейн
            AddBlock(block);

            // сохранение блока в файл
            BlockService.WriteBlockToFile(block);

            prevHash = currentHash;
        }
    }

    @Override
    public void AddBlock(BlockModel block){
        blockChain.add(block);
    }

    @Override
    public void MakeDamage(int blockNumber) throws IOException {
        var block = blockChain.get(blockNumber);

        block.GetData().SetData("What is it?!");

        BlockService.WriteBlockToFile(block);
    }

    @Override
    public void Print() throws NoSuchAlgorithmException {
        for (int i = 0; i < blockChainLength; i++) {
            var block = blockChain.get(i);
            System.out.println("~~~~~~~~~ " + block.GetBlockNumber() + " ~~~~~~~~~");
            System.out.println("prev hash: " + (block.GetPrevHash() != null ? new String(Hex.encode(block.GetPrevHash())) : ""));
            System.out.println("data: ");
            System.out.println(block.GetStringData());
            System.out.println("data signature: " + new String(Hex.encode(block.GetDataSign())));
            System.out.println("hash: " + new String(Hex.encode(BlockChainSignedWithArbiterUtility.GetHash(block))));
            System.out.println("arbiter signature: " + new String(Hex.encode(block.GetArbiterSign())));
            System.out.println();
        }
    }

    @Override
    public boolean VerifyBlock(int blockNumber) throws Exception {
        var prevHash = blockNumber > 0
                ? BlockChainSignedWithArbiterUtility.GetHash(blockChain.get(blockNumber - 1))
                : null;

        var currentBlock = blockChain.get(blockNumber);
        var currentBlockNumber = currentBlock.GetBlockNumber();

        // если хеш предыдущего блока и его значение в текущем блоке различаются,
        // то ошибка
        if (currentBlockNumber > 0 && !Arrays.equals(prevHash, currentBlock.GetPrevHash())) {
            return false;
        }

        // если подпись данных текущего блока не валидна,
        // то ошибка
        if (!BlockChainSignedWithArbiterUtility.VerifyDataSignature(keyPair.getPublic(), currentBlock.GetStringData().getBytes(StandardCharsets.UTF_8), currentBlock.GetDataSign())) {
            return false;
        }

        // вычисляем хеш текущего блока
        var currentHash = BlockChainSignedWithArbiterUtility.GetHash(currentBlock);

        // если подпись арбитра текущего блока не валидна,
        // то ошибка
        return ArbiterUtility.VerifyArbiterSignature(arbiterKey, currentHash, currentBlock.GetArbiterSignTimestamp(), currentBlock.GetArbiterSign());
    }

    @Override
    public boolean Verify() throws Exception {
        for (int i = 0; i < blockChainLength; i++) {
            if (!VerifyBlock(i))
                return false;
        }

        return true;
    }
}
