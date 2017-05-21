package me.shawlaf.banmanager.managers.database.util;

import me.shawlaf.banmanager.managers.database.AbstractSqlTable;

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
    
    private List<String> parametersToUpdate, parametersToCheck;
    private List<Object> valuesToUpdate, valuesToCheck;
    
    public static DatabaseUpdate create() {
        return new DatabaseUpdate();
    }
    
    private DatabaseUpdate() {
        this.parametersToCheck = new ArrayList<>();
        this.parametersToUpdate = new ArrayList<>();
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
        parametersToCheck.addAll(Arrays.asList(parameters));
        return this;
    }
    
    public DatabaseUpdate updateColumns(String... parameters) {
        parametersToUpdate.addAll(Arrays.asList(parameters));
        return this;
    }
    
    public String generateSqlString(String table) {
        
        if (valuesToUpdate.size() != parametersToUpdate.size() || valuesToCheck.size() != parametersToCheck.size())
            return null;
        
        StringBuilder sqlBuilder = new StringBuilder("UPDATE " + table + " SET ");
        
        for (int i = 0; i < parametersToUpdate.size(); ++ i)
            sqlBuilder.append(parametersToUpdate.get(i) + " = ?" + (i == parametersToUpdate.size() - 1 ? " " : ", "));
        
        if (parametersToCheck.size() > 0) {
            sqlBuilder.append("WHERE ");
            
            for (int i = 0; i < parametersToCheck.size(); ++ i)
                sqlBuilder.append(parametersToCheck.get(i) + " = ?" + (i == parametersToCheck.size() - 1 ? "" : " AND "));
        }
        
        sqlBuilder.append(";");
        
        return sqlBuilder.toString();
    }
    
    public Integer execute(Connection connection, String table) throws SQLException {
        int index = 1;
        PreparedStatement preparedStatement = connection.prepareStatement(generateSqlString(table));
        
        for (int i = 0; i < parametersToUpdate.size(); i++)
            preparedStatement.setObject(index++, valuesToUpdate.get(i));
        
        for (int i = 0; i < parametersToCheck.size(); i++)
            preparedStatement.setObject(index++, valuesToCheck.get(i));
        
        return preparedStatement.executeUpdate();
    }
    
}
