package me.shawlaf.banmanager.managers.database.util;

import me.shawlaf.banmanager.managers.database.AbstractSqlTable;

import javax.xml.transform.Result;
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
            for (int i = 0; i < columnsToCheck.size(); ++ i)
                sqlBuilder.append(columnsToCheck.get(i) + (i == columnsToCheck.size() - 1 ? " = ?" : " = ?, "));
            
        }
        
        sqlBuilder.append(";");
        return sqlBuilder.toString();
    }
    
    public ResultSet execute(AbstractSqlTable table) throws SQLException {
        PreparedStatement statement = table.connection().prepareStatement(generateSql(table.table));
        
        if (checkObjects != null && checkObjects.size() > 0) {
            int count = 1;
            
            for (Object checkValue : checkObjects)
                statement.setObject(count++, checkValue);
        }
        
        return statement.executeQuery();
    }
    
    public ResultSet execute(Connection connection, String table) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(generateSql(table));
        
        if (checkObjects != null && checkObjects.size() > 0) {
            int count = 1;
            
            for (Object checkValue : checkObjects)
                statement.setObject(count++, checkValue);
        }
        
        return statement.executeQuery();
    }
    
    
}
