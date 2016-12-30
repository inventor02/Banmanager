package me.shawlaf.banmanager.managers.database.util;

import me.shawlaf.banmanager.managers.database.AbstractSqlTable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Florian on 30.12.2016.
 */
public class DatabaseInsert {
    
    private Map<Integer, Object> map;
    private int index;
    
    public DatabaseInsert() {
        this(new HashMap<>());
    }
    
    public DatabaseInsert(Map<Integer, Object> map) {
        this.map = map;
        
        this.index = map.size() + 1;
    }
    
    public void put(Object... objects) {
        for (Object object : objects)
            map.put(index++, object);
    }
    
    public Map<Integer, Object> getMap() {
        return map;
    }
    
    public int size() {
        return map.size();
    }
    
    public Set<Map.Entry<Integer, Object>> entrySet() {
        return map.entrySet();
    }
    
    public void execute(AbstractSqlTable table) throws SQLException {
        String sql = "INSERT INTO " + table.table + " VALUES (" + generateQuestionMarks(size()) + ");";
    
        PreparedStatement preparedStatement = table.connection().prepareStatement(sql);
    
        for (Map.Entry<Integer, Object> entry : entrySet())
            preparedStatement.setObject(entry.getKey(), entry.getValue());
    
        preparedStatement.executeUpdate();
    }
    
    private String generateQuestionMarks(int amount) {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < amount; i++)
            builder.append(i == (amount - 1) ? "?" : "?, ");
        
        return builder.toString();
    }
}
