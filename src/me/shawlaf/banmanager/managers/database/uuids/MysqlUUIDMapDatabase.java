package me.shawlaf.banmanager.managers.database.uuids;

import me.shawlaf.banmanager.managers.database.AbstractSqlTable;
import me.shawlaf.banmanager.managers.database.DatabaseManager;
import me.shawlaf.banmanager.managers.database.UUIDMapDatabase;
import me.shawlaf.banmanager.managers.database.util.DatabaseDelete;
import me.shawlaf.banmanager.managers.database.util.DatabaseInsert;
import me.shawlaf.banmanager.managers.database.util.DatabaseQuery;
import net.md_5.bungee.api.ProxyServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Florian on 31.12.2016.
 */
public class MysqlUUIDMapDatabase extends AbstractSqlTable implements UUIDMapDatabase {
    
    private Map<String, UUID> nameToUUIDCache = new HashMap<>();
    private Map<UUID, String> uuidToNameCache = new HashMap<>();
    
    public MysqlUUIDMapDatabase(DatabaseManager databaseManager, String table) {
        super(databaseManager, table);
    
        int cacheClear = databaseManager.getPlugin().getConfiguration().getMysqlCacheClear();
        ProxyServer.getInstance().getScheduler().schedule(databaseManager.getPlugin(), this::clearCache, cacheClear, cacheClear, TimeUnit.MINUTES);
    }
    
    private void clearCache() {
        nameToUUIDCache.clear();
        uuidToNameCache.clear();
    }
    
    private String updateCache(String name, UUID uuid) {
        String oldName = uuidToNameCache.get(uuid);
        nameToUUIDCache.put(name.toLowerCase(), uuid);
        uuidToNameCache.put(uuid, name.toLowerCase());
        
        return oldName;
        
    }
    
    @Override
    public void updateNameUUIDSet(String name, UUID uuid) throws SQLException {
        String oldName = updateCache(name, uuid);
        
        DatabaseDelete.create().checkColumns("name").checkValues(oldName.toLowerCase()).execute(this);
        DatabaseInsert.create().put(name, uuid.toString()).execute(this);
    }
    
    @Override
    public UUID getUUID(String name) {
        if (nameToUUIDCache.containsKey(name.toLowerCase()))
            return nameToUUIDCache.get(name.toLowerCase());
        else {
            try {
                ResultSet set = DatabaseQuery.create().checkColumns("name").checkValues(name.toLowerCase()).selectColumns("uuid").execute(this);
                
                if (set.next()) {
                    UUID uuid = UUID.fromString(set.getString("uuid"));
                    updateNameUUIDSet(name, uuid);
                    
                    return uuid;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    @Override
    public String getName(UUID uuid) {
        if (uuidToNameCache.containsKey(uuid))
            return uuidToNameCache.get(uuid);
        else {
            try {
                ResultSet set = DatabaseQuery.create().checkColumns("uuid").checkValues(uuid.toString()).selectColumns("name").execute(this);
                
                if (set.next()) {
                    String name = set.getString("name");
                    updateNameUUIDSet(name, uuid);
                    
                    return name;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
    
    @Override
    protected String tableParams() {
        return "name varchar(16), uuid varchar(36)";
    }
}
