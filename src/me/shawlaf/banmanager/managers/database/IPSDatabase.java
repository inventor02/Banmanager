package me.shawlaf.banmanager.managers.database;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Florian on 26.03.2017.
 */
public interface IPSDatabase {
    
    void putIP(UUID uuid, String ip);
    
    Set<UUID> getUsersWithIP(String ip);
    
    Set<String> getIPS(UUID uuid);
    
    boolean has(UUID uuid) throws SQLException;
    
}
