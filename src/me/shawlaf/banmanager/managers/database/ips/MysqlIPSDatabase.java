package me.shawlaf.banmanager.managers.database.ips;

import me.shawlaf.banmanager.managers.database.AbstractUpdatedSqlTable;
import me.shawlaf.banmanager.managers.database.DatabaseManager;
import me.shawlaf.banmanager.managers.database.IPSDatabase;
import me.shawlaf.banmanager.managers.database.util.DatabaseInsert;
import me.shawlaf.banmanager.managers.database.util.DatabaseQuery;
import org.json.JSONArray;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Florian on 26.03.2017.
 */
public class MysqlIPSDatabase extends AbstractUpdatedSqlTable<UUID, Set<String>> implements IPSDatabase {
    
    public MysqlIPSDatabase(DatabaseManager databaseManager, String table) {
        super(databaseManager, table);
    }
    
    @Override
    public void putIP(UUID uuid, String ip) {
        try {
            DatabaseInsert.create().put(uuid.toString(), ip).execute(this);
            
            
            if (!hasCachedValue(uuid)) {
                putCache(uuid, getIPS(uuid));
            } else {
                Set<String> cachedIPS = getCachedValue(uuid);
                cachedIPS.add(ip);
                
                putCache(uuid, cachedIPS);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Set<UUID> getUsersWithIP(String ip) {
        
        try {
            
            Set<UUID> set = new HashSet<>();
            ResultSet resultSet = DatabaseQuery.create().selectColumns("uuid").checkColumns("ip").checkValues(ip).execute(this);
            
            while (resultSet.next()) {
                set.add(UUID.fromString(resultSet.getString("uuid")));
            }
            
            return set;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public Set<String> getIPS(UUID uuid) {
        try {
            
            if (hasCachedValue(uuid))
                return getCachedValue(uuid);
            
            Set<String> set = new HashSet<>();
            ResultSet resultSet = DatabaseQuery.create().selectColumns("ip").checkColumns("uuid").checkValues(uuid.toString()).execute(this);
            
            while (resultSet.next()) {
                set.add(resultSet.getString("ip"));
            }
            
            return set;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public boolean has(UUID uuid) throws SQLException {
        return hasCachedValue(uuid) || DatabaseQuery.create().checkColumns("uuid").checkValues(uuid.toString()).selectColumns("ip").execute(this).next();
    }
    
    @Override
    protected String tableParams() {
        return "uuid varchar(36), ip varchar(15)";
    }
    
    @Override
    protected boolean isOldFormat(String[][] columns) {
        return columns.length == 2 && columns[0][0].equals("uuid") && columns[0][1].equals("VARCHAR") && columns[1][0].equals("array") && columns[1][1].equals("LONGTEXT");
    }
    
    @Override
    protected DatabaseInsert[] convertToNew(ResultSet old) throws SQLException {
        Set<DatabaseInsert> inserts = new HashSet<>();
        
        while (old.next()) {
            String id = old.getString("uuid");
            JSONArray array = new JSONArray(old.getString("array"));
            
            for (int i = 0; i < array.length(); i++) {
                inserts.add(DatabaseInsert.create().put(id, array.get(i)));
            }
        }
        
        return inserts.toArray(new DatabaseInsert[inserts.size()]);
    }
}
