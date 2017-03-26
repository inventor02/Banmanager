package me.shawlaf.banmanager.managers.database;

import me.shawlaf.banmanager.managers.database.util.DatabaseInsert;
import me.shawlaf.banmanager.managers.database.util.DatabaseQuery;
import net.md_5.bungee.api.ProxyServer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by Florian on 31.12.2016.
 */
public abstract class AbstractUpdatedSqlTable extends AbstractSqlTable {
    
    public AbstractUpdatedSqlTable(DatabaseManager databaseManager, String table) {
        super(databaseManager, table);
        
        try {
            ResultSet allTables = connection().prepareStatement("SHOW TABLES;").executeQuery();
            
            while (allTables.next()) {
                if (table.equals(allTables.getString(1))) {
                    
                    ResultSet entireTable = DatabaseQuery.create().selectColumns(DatabaseQuery.SELECT_ALL).executeQuery(this);
                    
                    ResultSetMetaData resultSetMetaData = entireTable.getMetaData();
                    
                    int amountOfColumns = resultSetMetaData.getColumnCount();
                    String[][] columns = new String[amountOfColumns][];
                    
                    for (int i = 1; i <= amountOfColumns; i++) {
                        columns[i - 1][0] = resultSetMetaData.getColumnName(i);
                        columns[i - 1][1] = resultSetMetaData.getColumnTypeName(i);
                    }
                    
                    if (isOldFormat(columns)) {
                        ProxyServer.getInstance().getLogger().info("Converting old " + table + " Table to the new format");
                        entireTable.beforeFirst();
                        
                        DatabaseInsert[] insertsToExecute = convertToNew(entireTable);
                        
                        connection().prepareStatement("DROP TABLE " + table + ";").executeUpdate();
                        connection().prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (" + tableParams() + ");");
                        
                        for (DatabaseInsert i : insertsToExecute) {
                            try {
                                i.execute(this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        
                        ProxyServer.getInstance().getLogger().info("Converted old " + table + " Table to the new format");
                    }
                    
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    protected abstract boolean isOldFormat(String[][] columns);
    
    protected abstract DatabaseInsert[] convertToNew(ResultSet old) throws SQLException;
}
