package me.shawlaf.banmanager.managers.database.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Florian on 30.12.2016.
 */
public class DatabaseDelete implements DatabaseExecuteable<Integer> {
    
    private List<String> columnsToCheck;
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
        columnsToCheck = Arrays.asList(columns);
        return this;
    }
    
    public String generateSqlString(String table) {
        
        if (valuesToCheck.size() != columnsToCheck.size())
            return null;
        
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM " + table);
        
        if (columnsToCheck.size() > 0) {
            sqlBuilder.append(" WHERE ");
            
            for (int i = 0; i < columnsToCheck.size(); ++ i)
                sqlBuilder.append(columnsToCheck.get(i) + " = ?" + (i == columnsToCheck.size() - 1 ? "" : " AND "));
        }
        
        sqlBuilder.append(";");
        
        return sqlBuilder.toString();
    }
    
    @Override
    public Integer execute(Connection connection, String table) throws SQLException {
        int index = 1;
        PreparedStatement preparedStatement = connection.prepareStatement(generateSqlString(table));
        
        for (int i = 0; i < columnsToCheck.size(); i++) {
            preparedStatement.setObject(index++, valuesToCheck.get(i));
        }
        
        System.out.println(preparedStatement);
        return preparedStatement.executeUpdate();
    }
    
}