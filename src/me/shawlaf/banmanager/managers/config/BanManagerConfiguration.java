package me.shawlaf.banmanager.managers.config;

import net.md_5.bungee.config.Configuration;

/**
 * Created by Florian on 29.12.2016.
 */
public class BanManagerConfiguration {
    
    private Configuration configuration;
    
    public BanManagerConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    
    public String getMyselUser() {
        return configuration.getString("mysql_user", "root");
    }
    
    public String getMysqlPass() {
        return configuration.getString("mysql_pass", "");
    }
    
    public String getMysqlHost() {
        return configuration.getString("mysql_host", "127.0.0.1");
    }
    
    public String getMysqlDatabase() {
        return configuration.getString("mysql_database", "shawlafs-banmanager");
    }
    
    public int getMysqlPort() {
        return configuration.getInt("mysql_port", 3306);
    }
    
    public boolean isUsingMysql() {
        return configuration.getBoolean("use_mysql", false);
    }
    
    public int getMysqlCacheClear() {
        return configuration.getInt("mysql_cache_clear", 5);
    }
    
    public String getCommandBmInfo() {
        return configuration.getString("commandBmInfo", "bminfo");
    }
    
    public String getCommandHelperCap() {
        return configuration.getString("commandHelperCap", "helpercap");
    }
    
    public String getCommandAllowedCommands() {
        return configuration.getString("commandAllowedCommands", "allowedcommands");
    }
    
    public String getCommandPunish() {
        return configuration.getString("commandPunish", "bm");
    }
    
    public String getCommandBan() {
        return configuration.getString("commandBan", "bban");
    }
    
    public String getCommandMute() {
        return configuration.getString("commandMute", "bmute");
    }
    
    public String getCommandKick() {
        return configuration.getString("commandKick", "bkick");
    }
    
    public String getCommandPermissions() {
        return configuration.getString("commandPermissions", "permissions");
    }
    
    public int getHelperCap() {
        return configuration.getInt("helpercap", 604800000);
    }
    
    public boolean getBackup() {
        return configuration.getBoolean("backup", true);
    }
}
