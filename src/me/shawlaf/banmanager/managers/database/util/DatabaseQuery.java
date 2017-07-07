package me.shawlaf.banmanager.managers.database.util;

import me.shawlaf.banmanager.managers.database.AbstractSqlTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Florian on 31.12.2016.
 */
public class DatabaseQuery implements DatabaseExecuteable<ResultSet> {
    
    public static final String SELECT_ALL = "*";
    
    private List<String> columnsToSelect, columnsToCheck;
    private List<Object> checkObjects;
    
    public static DatabaseQuery create() {
        return new DatabaseQuery();
    }
    
    private DatabaseQuery() {
        // SELECT <columns> from <table> WHERE <columns values check>
    }
    
    public DatabaseQuery selectColumns(String... columns) {
        columnsToSelect = Arrays.asList(columns);
        return this;
    }
    
    public DatabaseQuery checkColumns(String... columns) {
        columnsToCheck = Arrays.asList(columns);
        return this;
    }
    
    public DatabaseQuery checkValues(Object... values) {
        checkObjects = Arrays.asList(values);
        return this;
    }
    
    private String generateSql(String table) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT ");
        
        for (int i = 0; i < columnsToSelect.size(); ++ i)
            sqlBuilder.append(columnsToSelect.get(i) + (i == columnsToSelect.size() - 1 ? " " : ", "));
        
        sqlBuilder.append("FROM " + table);
        
        if (columnsToCheck != null && columnsToCheck.size() > 0) {
            sqlBuilder.append(" WHERE ");
            
            for (int i = 0; i < columnsToCheck.size(); ++ i)
                sqlBuilder.append(columnsToCheck.get(i) + (i == columnsToCheck.size() - 1 ? " = ?" : " = ? AND "));
            
        }
        
        sqlBuilder.append(";");
        return sqlBuilder.toString();
    }
    
    public ResultSet execute(AbstractSqlTable table) throws SQLException {
        return execute(table.connection(), table.table);
    }
    
    public ResultSet execute(Connection connection, String table) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(generateSql(table));
        
        int index = 1;
        
        if (checkObjects != null && ! checkObjects.isEmpty()) {
            for (int i = 0; i < columnsToCheck.size(); i++) {
                statement.setObject(index++, checkObjects.get(i));
            }
        }
        
        System.out.println(statement);
        return statement.executeQuery();
    }
    
    
}