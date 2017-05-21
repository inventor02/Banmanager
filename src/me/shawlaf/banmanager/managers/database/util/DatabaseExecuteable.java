package me.shawlaf.banmanager.managers.database.util;

import me.shawlaf.banmanager.managers.database.AbstractSqlTable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Florian on 21.05.2017.
 */
public interface DatabaseExecuteable<T> {
    
    default T execute(AbstractSqlTable table) throws SQLException {
        return execute(table.connection(), table.table);
    }
    
    T execute(Connection connection, String table) throws SQLException;
    
}
