package me.shawlaf.banmanager.managers.database;

import me.shawlaf.banmanager.managers.database.util.DatabaseInsert;
import me.shawlaf.banmanager.managers.database.util.DatabaseQuery;
import net.md_5.bungee.api.ProxyServer;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
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
                
                if (! allTables.getString(1).equals(table))
                    continue;
                
                DatabaseMetaData databaseMetaData = connection().getMetaData();
                ResultSet columnsSet = databaseMetaData.getColumns(null, null, allTables.getString(1), null);
                
                int amountOfColumns = 0;
                
                while (columnsSet.next())
                    amountOfColumns++;
                
                columnsSet.beforeFirst();
                
                String[][] columns = new String[amountOfColumns][2];
                
                for (int i = 0; columnsSet.next(); i++) {
                    columns[i][0] = columnsSet.getString("COLUMN_NAME");
                    columns[i][1] = columnsSet.getString("TYPE_NAME");
                }
                
                if (isOldFormat(columns)) {
                    ProxyServer.getInstance().getLogger().info("Converting old " + table + " Table to the new format");
                    ResultSet entireOldTable = DatabaseQuery.create().selectColumns(DatabaseQuery.SELECT_ALL).execute(connection(), allTables.getString(1));
                    
                    DatabaseInsert[] insertsToExecute = convertToNew(entireOldTable);
                    
                    connection().prepareStatement("DROP TABLE " + table + ";").executeUpdate();
                    connection().prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (" + tableParams() + ");").executeUpdate();
                    
                    for (DatabaseInsert insert : insertsToExecute) {
                        try {
                            insert.execute(this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    
                    ProxyServer.getInstance().getLogger().info("Converted old " + table + " Table to the new format");
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
