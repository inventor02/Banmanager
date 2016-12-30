package me.shawlaf.banmanager.managers.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Florian on 30.12.2016.
 */
public abstract class AbstractSqlTable {
    
    protected DatabaseManager databaseManager;
    
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
    }
    
    protected abstract String tableParams();
    
    public Connection connection() {
        return databaseManager.getConnectionManager().getConnection();
    }
    
}
