package dk.wavebleak.wavespluginlib.database;

import java.sql.SQLException;

@SuppressWarnings("all")
interface ISQLiteManager {
    SQLDatabaseConnectionManager getDatabase();

    String[] getTables();

    void setTables(String[] tables);

    default void createTables(Runnable runnable) {
        getDatabase().connect(connection -> {
            try {
                connection.createStatement().executeUpdate(String.join(" ", getTables()));
                runnable.run();
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}

@SuppressWarnings("unused")
public class SQLiteManager implements ISQLiteManager {

    private final SQLDatabaseConnectionManager connectionManager;
    private String[] tables;

    public SQLiteManager(SQLDatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    @Override
    public SQLDatabaseConnectionManager getDatabase() {
        return connectionManager;
    }

    @Override
    public String[] getTables() {
        return tables;
    }

    @Override
    public void setTables(String[] tables) {
        this.tables = tables;
    }
}
