package dk.wavebleak.wavespluginlib.database;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class JsonArrayManager<T> {
    private File dataFile = null;
    private JsonArray dataArray;
    private Gson gson = null;
    private JavaPlugin instance = null;
    private final Class<T> type;
    public JsonArrayManager(Class<T> type) {
        dataFile = null;
        gson = null;
        this.type = type;
    }

    public JsonArrayManager setInstance(JavaPlugin instance) {
        this.instance = instance;
        return this;
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
        if(instance == null) throw new JsonManagerException("Instance is null");
        if(gson == null) throw new JsonManagerException("Gson is null");
        if(dataFile == null) throw new JsonManagerException("Datafile is null");
        if(!instance.getDataFolder().exists()) instance.getDataFolder().mkdir();
        if(!dataFile.exists()) {
            createDataFile();
        }
        dataArray = loadJsonFromFile(dataFile);
    }

    private void createDataFile() {
        try {
            if(dataFile.createNewFile()) {
                try(FileWriter writer = new FileWriter(dataFile)) {
                    writer.write("[]");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        List<T> list = gson.fromJson(dataArray, type);

        if(list == null) return new ArrayList<>();
        return list;
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
