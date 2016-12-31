package me.shawlaf.banmanager.managers.database;

import me.shawlaf.banmanager.managers.database.util.DatabaseInsert;
import me.shawlaf.banmanager.managers.database.util.DatabaseQuery;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

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
                        entireTable.beforeFirst();
                        
                        DatabaseInsert[] insertsToExecute = convertToNew(entireTable);
                        
                        connection().prepareStatement("DROP TABLE " + table + ";").executeUpdate();
    
                        for (DatabaseInsert i : insertsToExecute)
                            i.execute(this);
                    }
                    
                    break;
                }
            }
            
            connection().prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (" + tableParams() + ");").executeUpdate();
            
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    protected abstract boolean isOldFormat(String[][] columns);
    
    protected abstract DatabaseInsert[] convertToNew(ResultSet old) throws SQLException;
}
