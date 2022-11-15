package Services;

import Models.BlockModel;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BlockService {
    /*public static void WriteBlockToFile(BlockModel block, byte[] hash) throws IOException {
        var blockNumber = block.GetBlockNumber();
        var prevHash = block.GetPrevHash();
        var stringPrevHash = prevHash == null ? "" : new String(Hex.encode(block.GetPrevHash()));
        var stringHash = new String(Hex.encode(hash));

        var fileModel = new BlockFileModel(blockNumber, stringPrevHash, stringHash);
        var filePath = GetFilePath(blockNumber);

        var gson = new Gson();
        var writer = new FileWriter(filePath);
        gson.toJson(fileModel, writer);
        writer.close();
    }*/

    public static void WriteBlockToFile(BlockModel block) throws IOException {
        var blockNumber = block.GetBlockNumber();
        var filePath = GetFilePath(blockNumber);

        var gson = new GsonBuilder().setPrettyPrinting().create();
        var writer = new FileWriter(filePath);
        gson.toJson(block, writer);
        writer.close();
    }

    /*public static BlockModel ReadBlockFromFile(int blockNumber) throws FileNotFoundException {
        var filePath = GetFilePath(blockNumber);

        var gson = new Gson();
        var fileModel = gson.fromJson(new FileReader(filePath), BlockFileModel.class);

        var block = new BlockModel(fileModel.GetBlockNumber());
        return block;
    }*/

    public static BlockModel ReadBlockFromFile(int blockNumber) throws IOException {
        var filePath = GetFilePath(blockNumber);

        var gson = new GsonBuilder().setPrettyPrinting().create();
        var reader = new FileReader(filePath);
        var block = gson.fromJson(reader, BlockModel.class);
        reader.close();

        return block;
    }

    private static String GetFilePath(int blockNumber) {
        return "src/Resources/block_" + blockNumber + ".json";
    }
}
