package me.shawlaf.banmanager.managers.database.sql;

import me.shawlaf.banmanager.managers.config.BanManagerConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Florian on 29.12.2016.
 */
public class SqlConnectionManager {
    
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private Connection connection;
    
    private String user, pass, host, database;
    private int port;
    
    public SqlConnectionManager(BanManagerConfiguration banManagerConfiguration) {
        this.user = banManagerConfiguration.getMyselUser();
        this.pass = banManagerConfiguration.getMysqlPass();
        this.host = banManagerConfiguration.getMysqlHost();
        this.database = banManagerConfiguration.getMysqlDatabase();
        this.port = banManagerConfiguration.getMysqlPort();
    }
    
    public String getUser() {
        return user;
    }
    
    public int getPort() {
        return port;
    }
    
    public String getDatabase() {
        return database;
    }
    
    public String getHost() {
        return host;
    }
    
    public String getPass() {
        return pass;
    }
    
    public Connection getConnection() {
        
        if (connection == null)
            return connect();
        
        return connection;
    }
    
    public Connection connect() {
        try {
            if (connection != null)
                connection.close();
            
            String connectUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
            
            connection = DriverManager.getConnection(connectUrl, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return connection;
    }
}
