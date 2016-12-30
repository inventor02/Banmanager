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
public class DatabaseUpdate {
    
    private List<String> parametersToUpdate, valuesToUpdate;
    private List<String> parametersToCheck, valuesToCheck;
    
    public DatabaseUpdate() {
        this.parametersToCheck = new ArrayList<>();
        this.parametersToUpdate = new ArrayList<>();
    }
    
    public DatabaseUpdate updateValues(String... values) {
        valuesToUpdate.addAll(Arrays.asList(values));
        return this;
    }
    
    public DatabaseUpdate checkValues(String... values) {
        valuesToCheck.addAll(Arrays.asList(values));
        return this;
    }
    
    public DatabaseUpdate checkColumns(String... parameters) {
        parametersToUpdate.addAll(Arrays.asList(parameters));
        return this;
    }
    
    public DatabaseUpdate updateColumns(String... parameters) {
        parametersToUpdate.addAll(Arrays.asList(parameters));
        return this;
    }
    
    public String generateSqlString(AbstractSqlTable table) {
        
        if (valuesToUpdate.size() != parametersToUpdate.size() || valuesToCheck.size() != parametersToCheck.size())
            return null;
        
        StringBuilder sqlBuilder = new StringBuilder("UPDATE " + table.table + " SET ");
        
        for (int i = 0; i < parametersToUpdate.size(); ++ i)
            sqlBuilder.append(parametersToUpdate.get(i) + " = ?" + (i == parametersToUpdate.size() - 1 ? " " : ", "));
        
        for (int i = 0; i < parametersToCheck.size(); ++ i)
            sqlBuilder.append(parametersToCheck.get(i) + " = ?" + (i == parametersToCheck.size() - 1 ? ";" : " AND "));
        
        return sqlBuilder.toString();
    }
    
    public void execute(AbstractSqlTable table) throws SQLException {
        int index = 1;
        PreparedStatement preparedStatement = table.connection().prepareStatement(generateSqlString(table));
        
        for (int i = 0; i < parametersToUpdate.size(); i++)
            preparedStatement.setObject(index++, valuesToUpdate.get(i));
        
        for (int i = 0; i < parametersToCheck.size(); i++)
            preparedStatement.setObject(index++, valuesToCheck.get(i));
        
        preparedStatement.executeUpdate();
    }
    
}
