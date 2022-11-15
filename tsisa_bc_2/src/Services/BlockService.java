package Services;

import Models.BlockModel;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BlockService {
    public static void WriteBlockToFile(BlockModel block) throws IOException {
        var blockNumber = block.GetBlockNumber();
        var filePath = GetFilePath(blockNumber);

        var gson = new GsonBuilder().setPrettyPrinting().create();
        var writer = new FileWriter(filePath);
        gson.toJson(block, writer);
        writer.close();
    }

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
