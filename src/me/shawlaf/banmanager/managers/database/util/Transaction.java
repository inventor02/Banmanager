package me.shawlaf.banmanager.managers.database.util;

import me.shawlaf.banmanager.managers.database.AbstractSqlTable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Florian on 21.05.2017.
 */
public class Transaction {
    
    public static Transaction createNew() {
        return new Transaction();
    }
    
    private List<DatabaseExecuteable> actions = new ArrayList<>();
    private List<Consumer> handlers = new ArrayList<>();
    
    private Transaction() {}
    
    public <T> Transaction addTask(DatabaseExecuteable<T> executeable, Consumer<T> handler) {
        synchronized (actions) {
            actions.add(executeable);
            handlers.add(handler);
        }
        
        return this;
    }
    
    public <T> Transaction addTask(DatabaseExecuteable<T> executeable) {
        return addTask(executeable, t -> {});
    }
    
    public void execute(AbstractSqlTable table) {
        Connection connection;
        
        synchronized (connection = table.connection()) {
            try {
                connection.setAutoCommit(false);
                
                for (int i = 0; i < actions.size(); i++) {
                    handlers.get(i).accept(actions.get(i).execute(table));
                }
                
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
            
        }
    }
}
