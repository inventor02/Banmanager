package me.shawlaf.banmanager.managers.database;

import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.managers.config.users.LocalUserDatabase;
import me.shawlaf.banmanager.managers.config.uuids.LocalUUIDMapDatabase;

/**
 * Created by Florian on 30.12.2016.
 */
public class DatabaseManager {
    
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
    
}
