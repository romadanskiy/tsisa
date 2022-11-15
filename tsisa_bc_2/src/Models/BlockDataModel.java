package Models;

import com.google.gson.GsonBuilder;

public class BlockDataModel {
    private String data;
    private String timestamp;

    public BlockDataModel(String data, String timestamp) {
        this.data = data;
        this.timestamp = timestamp;
    }

    public String toString() {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public String GetData() {
        return data;
    }

    public void SetData(String data) {
        this.data = data;
    }

    public String GetTimestamp() {
        return timestamp;
    }

    public void SetTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
