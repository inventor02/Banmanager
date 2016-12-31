package me.shawlaf.banmanager.managers.database;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Florian on 29.12.2016.
 */
public interface UUIDMapDatabase {
    
    void updateNameUUIDSet(String name, UUID uuid) throws SQLException;
    
    UUID getCachedUUID(String name);
    
    String getCachedName(UUID uuid);
}
