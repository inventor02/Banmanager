package me.shawlaf.banmanager.managers.database;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Florian on 29.12.2016.
 */
public interface UUIDMapDatabase {
    
    void updateNameUUIDSet(String name, UUID uuid) throws SQLException;
    
    UUID getUUID(String name);
    
    String getName(UUID uuid);
    
    default boolean hasName(String name) {
        return getUUID(name) != null;
    }
    
    default boolean hasUUID(UUID uuid) {
        return getName(uuid) != null;
    }
}
