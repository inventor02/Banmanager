package me.shawlaf.banmanager.managers.database.punish;

import me.shawlaf.banmanager.managers.database.AbstractSqlTable;
import me.shawlaf.banmanager.managers.database.DatabaseManager;
import me.shawlaf.banmanager.managers.database.PunishmentDatabase;
import me.shawlaf.banmanager.managers.database.util.DatabaseDelete;
import me.shawlaf.banmanager.managers.database.util.DatabaseInsert;
import me.shawlaf.banmanager.managers.database.util.DatabaseQuery;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        try {
            ResultSet set = DatabaseQuery.create().checkColumns("id").checkValues(punishmentId.toString()).executeQuery(this);
            
            JSONObject object = new JSONObject();
            
            int pointer = 1;
            
            object.put("reason", set.getString(pointer++));
            object.put("offender", set.getString(pointer++));
            object.put("moderator", set.getString(pointer++));
            object.put("dateCreated", set.getLong(pointer++));
            object.put("type", set.getInt(pointer++));
            object.put("id", set.getString(pointer++));
            object.put("modip", set.getString(pointer++));
            
            String removeReason = set.getString(pointer++);
            long dateRemoved = set.getLong(pointer++);
            String removedBy = set.getString(pointer++);
            
            boolean scheduledRemove = set.getBoolean(pointer++);
            
            JSONObject addTo = scheduledRemove ? new JSONObject() : object;
            
            addTo.put("removeReason", removeReason);
            addTo.put("dateRemoved", dateRemoved);
            addTo.put("removedBy", removedBy);
            
            if (scheduledRemove) {
                object.put("removeWhen", addTo);
            }
            
            object.put("length", set.getLong(pointer++));
            
            return object;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public void wipePunishment(UUID punishmentId) {
        try {
            DatabaseDelete.create().checkColumns("id").checkValues(punishmentId.toString()).execute(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void putPunishment(UUID punishmentId, JSONObject object) {
        try {
            DatabaseInsert.create().put(
                    object.getString("reason"),
                    object.getString("offender"),
                    object.getString("moderator"),
                    object.getLong("dateCreated"),
                    object.getInt("type"),
                    object.getString("id"),
                    object.getString("modip"),
                    object.optString("removeReason"),
                    object.optLong("dateRemoved"),
                    object.optString("removedBy"),
                    object.has("removeWhen"),
                    object.optLong("length")
            ).execute(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean doesPunishmentExist(UUID punishmentId) {
        try {
            return DatabaseQuery.create().checkColumns("id").checkValues(punishmentId.toString()).executeQuery(this).next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    protected String tableParams() {
        return "reason varchar(100), " +
                "offender varchar(36), " +
                "moderator varchar(36), " +
                "dateCreated bigint, " +
                "type int, " +
                "id varchar(36), " +
                "modip varchar(15), " +
                "removeReason varchar(36), " +
                "dateRemoved bigint, " +
                "removedBy varchar(36), " +
                "scheduledRemove tinyint(1), " +
                "length bigint";
    }
}
