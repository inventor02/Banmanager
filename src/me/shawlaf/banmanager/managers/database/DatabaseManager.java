package me.shawlaf.banmanager.managers.database;

import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.managers.config.ips.LocalIPSDatabase;
import me.shawlaf.banmanager.managers.config.punish.LocalPunishmentDatabase;
import me.shawlaf.banmanager.managers.config.users.LocalUserDatabase;
import me.shawlaf.banmanager.managers.config.uuids.LocalUUIDMapDatabase;
import me.shawlaf.banmanager.managers.database.ips.MysqlIPSDatabase;
import me.shawlaf.banmanager.managers.database.punish.MysqlPunishmentDatabase;
import me.shawlaf.banmanager.managers.database.sql.SqlConnectionManager;
import me.shawlaf.banmanager.managers.database.users.MysqlUserDatabase;
import me.shawlaf.banmanager.managers.database.uuids.MysqlUUIDMapDatabase;

/**
 * Created by Florian on 30.12.2016.
 */
public class DatabaseManager {
    
    private SqlConnectionManager connectionManager;
    
    private Banmanager banmanager;
    
    private UserDatabase userDatabase;
    private UUIDMapDatabase uuidMapDatabase;
    private PunishmentDatabase punishmentDatabase;
    private IPSDatabase ipsDatabase;
    
    public DatabaseManager(Banmanager banmanager) {
        this.banmanager = banmanager;
        this.connectionManager = new SqlConnectionManager(banmanager.getConfiguration());
        
        if (banmanager.getConfiguration().isUsingMysql()) {
            uuidMapDatabase = new MysqlUUIDMapDatabase(this, "uuids");
            userDatabase = new MysqlUserDatabase(this, "users");
            punishmentDatabase = new MysqlPunishmentDatabase(this, "punishments");
            ipsDatabase = new MysqlIPSDatabase(this, "ips");
        } else {
            userDatabase = new LocalUserDatabase(banmanager.getConfigurationManager().getUsersFile());
            uuidMapDatabase = new LocalUUIDMapDatabase(banmanager.getConfigurationManager().getUUUIDMapFile());
            punishmentDatabase = new LocalPunishmentDatabase(banmanager.getConfigurationManager().getPunishmentsFile());
            ipsDatabase = new LocalIPSDatabase(banmanager.getConfigurationManager().getIpsFile());
        }
    }
    
    public PunishmentDatabase getPunishmentDatabase() {
        return punishmentDatabase;
    }
    
    public Banmanager getPlugin() {
        return banmanager;
    }
    
    public SqlConnectionManager getConnectionManager() {
        return connectionManager;
    }
    
    public UserDatabase getUserDatabase() {
        return userDatabase;
    }
    
    public UUIDMapDatabase getUuidMapDatabase() {
        return uuidMapDatabase;
    }
}
