package me.shawlaf.banmanager.managers.database;

import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.managers.config.users.LocalUserDatabase;
import me.shawlaf.banmanager.managers.config.uuids.LocalUUIDMapDatabase;
import me.shawlaf.banmanager.managers.database.sql.SqlConnectionManager;

/**
 * Created by Florian on 30.12.2016.
 */
public class DatabaseManager {
    
    private SqlConnectionManager connectionManager;
    
    private Banmanager banmanager;
    
    private UserDatabase userDatabase;
    private UUIDMapDatabase uuidMapDatabase;
    
    public DatabaseManager(Banmanager banmanager) {
        this.banmanager = banmanager;
        
        if (banmanager.getConfiguration().isUsingMysql()) {
            // TODO write implementation for Mysql Database
        } else {
            userDatabase = new LocalUserDatabase(banmanager.getConfigurationManager().getUsersFile());
            uuidMapDatabase = new LocalUUIDMapDatabase(banmanager.getConfigurationManager().getUUUIDMapFile());
        }
    }
    
    public SqlConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
