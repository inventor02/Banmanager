package me.shawlaf.banmanager.managers.database;

import jdk.nashorn.internal.ir.ReturnNode;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.scheduler.BungeeScheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Florian on 30.12.2016.
 */
public abstract class AbstractSqlTable<K, V> {
    
    protected DatabaseManager databaseManager;
    private Map<K, V> cache = new HashMap<>();
    public final String table;
    
    public AbstractSqlTable(DatabaseManager databaseManager, String table) {
        this.databaseManager = databaseManager;
        
        this.table = table;
        
        try {
            PreparedStatement statement = connection().prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (" + tableParams() + ");");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        ProxyServer.getInstance().getScheduler().schedule(
                
                databaseManager.getPlugin(),
                cache::clear,
                
                databaseManager
                        .getPlugin()
                        .getConfigurationManager()
                        .getConfiguration()
                        .getMysqlCacheClear(),
                
                TimeUnit.MINUTES
        
        );
    }
    
    protected void putCache(K key, V value) {
        cache.put(key, value);
    }
    
    protected V getCachedValue(K key) {
        return cache.get(key);
    }
    
    protected boolean hasCachedValue(K key) {
        return cache.containsKey(key);
    }
    
    protected void removeCachedValue(K key) {
        cache.remove(key);
    }
    
    protected abstract String tableParams();
    
    public Connection connection() {
        return databaseManager.getConnectionManager().getConnection();
    }
    
}
