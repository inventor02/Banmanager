package me.shawlaf.banmanager.managers.database.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Florian on 30.12.2016.
 */
public class DatabaseInsert implements DatabaseExecuteable<Integer> {
    
    private Map<Integer, Object> map;
    private int index;
    
    public static DatabaseInsert create() {
        return create(new HashMap<>());
    }
    
    public static DatabaseInsert create(Map<Integer, Object> map) {
        return new DatabaseInsert(map);
    }
    
    private DatabaseInsert(Map<Integer, Object> map) {
        this.map = map;
        
        this.index = map.size() + 1;
    }
    
    public DatabaseInsert put(Object... objects) {
        for (Object object : objects)
            map.put(index++, object);
        
        return this;
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
    
    @Override
    public Integer execute(Connection connection, String table) throws SQLException {
        String sql = "INSERT INTO " + table + " VALUES (" + generateQuestionMarks(size()) + ");";
        
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int index = 1;
        
        preparedStatement.setString(index++, table);
        
        for (Map.Entry<Integer, Object> entry : entrySet()) {
            preparedStatement.setObject(entry.getKey(), entry.getValue());
        }
        
        System.out.println(preparedStatement);
        return preparedStatement.executeUpdate();
    }
    
    private String generateQuestionMarks(int amount) {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < amount; i++)
            builder.append(i == (amount - 1) ? "?" : "?, ");
        
        return builder.toString();
    }
}