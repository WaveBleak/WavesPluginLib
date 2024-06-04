package dk.wavebleak.wavespluginlib.database;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import dk.wavebleak.wavespluginlib.WavesPluginLib;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class JsonArrayManager<T> {
    private File dataFile;
    private JsonArray dataArray;
    private Gson gson;
    private final JavaPlugin instance;
    private final Class<T> type;
    private final T example;
    public JsonArrayManager(Class<T> type, T example) {
        this.instance = WavesPluginLib.pluginInstance;
        if(!instance.getDataFolder().exists()) instance.getDataFolder().mkdir();
        dataFile = null;
        gson = null;
        this.type = type;
        this.example = example;
    }

    public JsonArrayManager setGson(Gson gson) {
        this.gson = gson;
        return this;
    }
    public JsonArrayManager setFile(String name) {
        this.dataFile = new File(instance.getDataFolder().toString() + File.separator + name + ".json");
        return this;
    }
    public void init() throws JsonManagerException {
        if(gson == null) throw new JsonManagerException("Gson is null");
        if(dataFile == null) throw new JsonManagerException("Datafile is null");
        if(!dataFile.exists()) {
            createDataFile();
        }
    }

    private void createDataFile() {
        try {
            if(dataFile.createNewFile()) {
                List<T> data = new ArrayList<>(Collections.singletonList(example));
                saveData(data);
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized List<T> refreshData() {
        dataArray = loadJsonFromFile(dataFile);

        return loadData();
    }

    public synchronized List<T> loadData() {
        Type type = new TypeToken<List<T>>() {}.getType();

        return gson.fromJson(dataArray, type);
    }

    public synchronized void saveData(List<T> data) {
        if(data == null || data.isEmpty()) return;

        dataArray = gson.toJsonTree(data).getAsJsonArray();
        saveJsonToFile(dataArray, dataFile);
    }

    private synchronized JsonArray loadJsonFromFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, JsonArray.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonArray();
    }
    private synchronized <T> void saveJsonToFile(T object, File dbFile) {
        try (FileWriter writer = new FileWriter(dbFile)) {
            String jsonString = gson.toJson(object);
            writer.write(jsonString);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
