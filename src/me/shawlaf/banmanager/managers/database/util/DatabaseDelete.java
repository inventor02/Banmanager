package me.shawlaf.banmanager.managers.database.util;

import me.shawlaf.banmanager.managers.database.AbstractSqlTable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Florian on 30.12.2016.
 */
public class DatabaseDelete {
    
    private List<String> parametersToCheck;
    private List<Object> valuesToCheck;
    
    public static DatabaseDelete create() {
        return new DatabaseDelete();
    }
    
    private DatabaseDelete() {}
    
    public DatabaseDelete checkValues(Object... values) {
        valuesToCheck = Arrays.asList(values);
        return this;
    }
    
    public DatabaseDelete checkColumns(String... columns) {
        parametersToCheck = Arrays.asList(columns);
        return this;
    }
    
    public String generateSqlString(AbstractSqlTable table) {
        
        if (valuesToCheck.size() != parametersToCheck.size())
            return null;
        
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM " + table.table);
        
        if (parametersToCheck.size() > 0) {
            sqlBuilder.append("WHERE ");
            
            for (int i = 0; i < parametersToCheck.size(); ++ i)
                sqlBuilder.append(parametersToCheck.get(i) + " = ?" + (i == parametersToCheck.size() - 1 ? "" : " AND "));
        }
        
        sqlBuilder.append(";");
        
        return sqlBuilder.toString();
    }
    
    public void execute(AbstractSqlTable table) throws SQLException {
        int index = 1;
        PreparedStatement preparedStatement = table.connection().prepareStatement(generateSqlString(table));
        
        for (int i = 0; i < parametersToCheck.size(); i++)
            preparedStatement.setObject(index++, valuesToCheck.get(i));
        
        preparedStatement.executeUpdate();
    }
    
}
