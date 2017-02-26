package me.shawlaf.banmanager.managers.database.punish;

import me.shawlaf.banmanager.indev.NotYetImplementedException;
import me.shawlaf.banmanager.managers.database.AbstractSqlTable;
import me.shawlaf.banmanager.managers.database.DatabaseManager;
import me.shawlaf.banmanager.managers.database.PunishmentDatabase;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Florian on 26.02.2017.
 */
public class MysqlPunishmentDatabase extends AbstractSqlTable implements PunishmentDatabase {
    
    public MysqlPunishmentDatabase(DatabaseManager databaseManager, String table) {
        super(databaseManager, table);
    }
    
    @Override
    public JSONObject getPunishmentObject(UUID punishmentId) {
        throw new NotYetImplementedException();
    }
    
    @Override
    public void wipePunishment(UUID punishmentId) {
        throw new NotYetImplementedException();
    }
    
    @Override
    public void putPunishment(UUID punishmentId, JSONObject object) {
        throw new NotYetImplementedException();
    }
    
    @Override
    public boolean doesPunishmentExist(UUID punishmentId) {
        throw new NotYetImplementedException();
    }
    
    @Override
    protected String tableParams() {
        return "reason varchar(100), offender varchar(36), moderator varchar(36), datecreated bigint, ";// TODO continue
    }
}
