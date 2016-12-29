package me.shawlaf.banmanager.managers.database.sql;

import me.shawlaf.banmanager.managers.config.BanManagerConfiguration;

/**
 * Created by Florian on 29.12.2016.
 */
public class SqlInformation {
    
    private String user, pass, host, database;
    private int port;
    
    public SqlInformation(BanManagerConfiguration banManagerConfiguration) {
        this.user = banManagerConfiguration.getMyselUser();
        this.pass = banManagerConfiguration.getMysqlPass();
        this.host = banManagerConfiguration.getMysqlHost();
        this.database = banManagerConfiguration.getMysqlDatabase();
        this.port = banManagerConfiguration.getMysqlPort();
    }
    
    
}
