package me.shawlaf.banmanager.managers.database.users;

import me.shawlaf.banmanager.managers.database.AbstractUpdatedSqlTable;
import me.shawlaf.banmanager.managers.database.DatabaseManager;
import me.shawlaf.banmanager.managers.database.UserDatabase;
import me.shawlaf.banmanager.managers.database.util.DatabaseDelete;
import me.shawlaf.banmanager.managers.database.util.DatabaseInsert;
import me.shawlaf.banmanager.managers.database.util.DatabaseQuery;
import me.shawlaf.banmanager.util.JSONUtils;
import net.md_5.bungee.api.ProxyServer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Florian on 31.12.2016.
 */
public class MysqlUserDatabase extends AbstractUpdatedSqlTable implements UserDatabase {
    
    private Map<UUID, JSONObject> cache = new HashMap<>();
    
    public MysqlUserDatabase(DatabaseManager databaseManager, String table) {
        super(databaseManager, table);
        
        int cacheClear = databaseManager.getPlugin().getConfiguration().getMysqlCacheClear();
        
        ProxyServer.getInstance().getScheduler().schedule(databaseManager.getPlugin(), this::clearCache, cacheClear, cacheClear, TimeUnit.MINUTES);
    }
    
    private void clearCache() {
        cache.clear();
    }
    
    @Override
    protected boolean isOldFormat(String[][] columns) {
        return columns.length == 2 && columns[0][0].equals("uuid") && columns[0][1].equals("VARCHAR") && columns[1][0].equals("obj") && columns[1][1].equals("LONGTEXT");
    }
    
    @Override
    protected DatabaseInsert[] convertToNew(ResultSet old) throws SQLException {
        Set<DatabaseInsert> databaseInsertSet = new HashSet<>();
        Set<JSONObject> oldDatabase = new HashSet<>();
        
        while (old.next())
            oldDatabase.add(new JSONObject(old.getString("obj")));
        
        for (JSONObject oldObject : oldDatabase)
            System.out.println(oldObject.toString(0));
        
        for (JSONObject object : oldDatabase)
            databaseInsertSet.add(toInsert(object));
        
        return databaseInsertSet.toArray(new DatabaseInsert[databaseInsertSet.size()]);
    }
    
    private JSONObject cacheUpdate(UUID uuid, JSONObject object) {
        cache.put(uuid, object);
        return object;
    }
    
    @Override
    public JSONObject getUserObject(UUID uuid) {
        try {
            if (cache.containsKey(uuid))
                return cache.get(uuid);
            
            ResultSet set = DatabaseQuery.create().selectColumns(DatabaseQuery.SELECT_ALL).checkColumns("uuid").checkValues(uuid.toString()).executeQuery(this);
            
            if (set.next()) {
                return cacheUpdate(uuid, constructJSONObject(set));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private JSONObject constructJSONObject(ResultSet set) throws SQLException {
        JSONObject object = new JSONObject();
        
        object.put("name", set.getString("name"));
        object.put("uuid", set.getString("uuid"));
        object.put("admin", set.getBoolean("admin"));
        object.put("mail", JSONUtils.toJSONArray(Arrays.asList((Object[]) set.getArray("mail").getArray())));
        
        return object;
    }
    
    private DatabaseInsert toInsert(JSONObject object) throws SQLException {
        JSONArray jsonArrayMail = object.optJSONArrayNotNull("mail");
        
        return DatabaseInsert.create().put(object.getString("name"), object.getString("uuid"), object.getBoolean("admin"), jsonArrayMail.toString(0));
    }
    
    @Override
    public void updateUser(UUID uuid, JSONObject object) {
        cacheUpdate(uuid, object);
        
        try {
            DatabaseDelete.create().checkColumns("uuid").checkValues(uuid.toString()).execute(this);
            toInsert(object).execute(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean has(UUID uuid) {
        if (cache.containsKey(uuid))
            return true;
        else {
            try {
                return DatabaseQuery.create().checkColumns("uuid").checkValues(uuid.toString()).executeQuery(this).next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
    
    @Override
    protected String tableParams() {
        return "name varchar(16), uuid varchar(36), admin tinyint(1), mail longtext";
    }
}
