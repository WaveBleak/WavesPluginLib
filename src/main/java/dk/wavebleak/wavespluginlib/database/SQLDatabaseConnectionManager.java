package dk.wavebleak.wavespluginlib.database;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class SQLDatabaseConnectionManager {

    private final JavaPlugin plugin;
    private final String connectionString;
    private final Lock lock = new ReentrantLock(true);
    private Connection connection;

    public SQLDatabaseConnectionManager(JavaPlugin plugin, String databaseName) {
        this.plugin = plugin;
        this.connectionString = String.format("jdbc:sqlite:%s%s%s.db", plugin.getDataFolder(), File.separator, databaseName);
        //File file = new File(String.format("%s%s%s", plugin.getDataFolder(), File.separator, databaseName));
        if(!plugin.getDataFolder().exists()) if(plugin.getDataFolder().mkdir()) {
            plugin.getLogger().info("Oprettede plugin folder til databasen!");
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().severe("Kunne ikke finde JDBC library!");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }


    public void connect(Consumer<Connection> callback) {
        asyncFuture(() -> {
            if(connection == null) {
                try {
                    connection = DriverManager.getConnection(connectionString);
                }catch (SQLException ex) {
                    plugin.getLogger().severe("Kunne ikke oprette forbindelse til databasen!");
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            }
            callback.accept(connection);
        });
    }

    public void syncConnect(Consumer<Connection> callback) {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(connectionString);
            } catch (SQLException ex) {
                //noinspection CallToPrintStackTrace
                ex.printStackTrace();
            }
        }
        callback.accept(connection);
    }

    public void close() throws SQLException {
        connection.close();
    }


    private CompletableFuture<Void> asyncFuture(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    runnable.run();
                }finally {
                    lock.unlock();
                }
                future.complete(null);
            }
        }.runTaskAsynchronously(plugin);
        return future;
    }

}
