package me.shawlaf.banmanager.managers.database.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Florian on 30.12.2016.
 */
public class DatabaseUpdate implements DatabaseExecuteable<Integer> {
    
    private List<String> columnsToUpdate, columnsToCheck;
    private List<Object> valuesToUpdate, valuesToCheck;
    
    public static DatabaseUpdate create() {
        return new DatabaseUpdate();
    }
    
    private DatabaseUpdate() {
        this.columnsToCheck = new ArrayList<>();
        this.columnsToUpdate = new ArrayList<>();
        this.valuesToUpdate = new ArrayList<>();
        this.valuesToCheck = new ArrayList<>();
    }
    
    public DatabaseUpdate updateValues(Object... values) {
        valuesToUpdate.addAll(Arrays.asList(values));
        return this;
    }
    
    public DatabaseUpdate checkValues(Object... values) {
        valuesToCheck.addAll(Arrays.asList(values));
        return this;
    }
    
    public DatabaseUpdate checkColumns(String... parameters) {
        columnsToCheck.addAll(Arrays.asList(parameters));
        return this;
    }
    
    public DatabaseUpdate updateColumns(String... parameters) {
        columnsToUpdate.addAll(Arrays.asList(parameters));
        return this;
    }
    
    public String generateSqlString(String table) {
        
        if (valuesToUpdate.size() != columnsToUpdate.size() || valuesToCheck.size() != columnsToCheck.size())
            return null;
        
        StringBuilder sqlBuilder = new StringBuilder("UPDATE " + table + " SET ");
        
        for (int i = 0; i < columnsToUpdate.size(); ++ i)
            sqlBuilder.append(columnsToUpdate.get(i) + " = ?" + (i == columnsToUpdate.size() - 1 ? " " : ", "));
        
        if (columnsToCheck.size() > 0) {
            sqlBuilder.append("WHERE ");
            
            for (int i = 0; i < columnsToCheck.size(); ++ i)
                sqlBuilder.append(columnsToCheck.get(i) + " = ?" + (i == columnsToCheck.size() - 1 ? "" : " AND "));
        }
        
        sqlBuilder.append(";");
        
        return sqlBuilder.toString();
    }
    
    public Integer execute(Connection connection, String table) throws SQLException {
        int index = 1;
        PreparedStatement preparedStatement = connection.prepareStatement(generateSqlString(table));
        
        for (int i = 0; i < columnsToUpdate.size(); i++) {
            preparedStatement.setObject(index++, valuesToUpdate.get(i));
        }
        
        for (int i = 0; i < columnsToCheck.size(); i++) {
            preparedStatement.setObject(index++, valuesToCheck.get(i));
        }
        
        System.out.println(preparedStatement);
        return preparedStatement.executeUpdate();
    }
    
}