package me.shawlaf.banmanager.managers.database.punish;

import me.shawlaf.banmanager.managers.database.AbstractUpdatedSqlTable;
import me.shawlaf.banmanager.managers.database.DatabaseManager;
import me.shawlaf.banmanager.managers.database.PunishmentDatabase;
import me.shawlaf.banmanager.managers.database.util.DatabaseDelete;
import me.shawlaf.banmanager.managers.database.util.DatabaseInsert;
import me.shawlaf.banmanager.managers.database.util.DatabaseQuery;
import me.shawlaf.banmanager.managers.database.util.DatabaseUpdate;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Florian on 26.02.2017.
 */
public class MysqlPunishmentDatabase extends AbstractUpdatedSqlTable<UUID, JSONObject> implements PunishmentDatabase {
    
    public MysqlPunishmentDatabase(DatabaseManager databaseManager, String table) {
        super(databaseManager, table);
    }
    
    @Override
    protected boolean isOldFormat(String[][] columns) {
        return columns.length == 2 && columns[0][0].equals("uuid") && columns[0][1].equals("VARCHAR") && columns[1][0].equals("obj") && columns[1][1].equals("LONGTEXT");
    }
    
    @Override
    protected DatabaseInsert[] convertToNew(ResultSet old) throws SQLException {
        Set<DatabaseInsert> updates = new HashSet<>();
        
        while (old.next()) {
            JSONObject punishmentObject = new JSONObject(old.getString("obj"));
            updates.add(toInsert(UUID.fromString(old.getString("uuid")), punishmentObject));
        }
        
        return updates.toArray(new DatabaseInsert[updates.size()]);
    }
    
    @Override
    public JSONObject getPunishmentObject(UUID punishmentId) {
        if (hasCachedValue(punishmentId))
            return getCachedValue(punishmentId);
        
        try (ResultSet set = DatabaseQuery.create().checkColumns("id").checkValues(punishmentId.toString()).execute(this)) {
            
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
            removeCachedValue(punishmentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public DatabaseInsert toInsert(UUID punishmentId, JSONObject object) {
        Object[] values = new Object[] {
                object.getString("reason"),
                object.getString("offender"),
                object.getString("moderator"),
                object.getLong("dateCreated"),
                object.getInt("type"),
                object.getString("id"),
                object.optString("modip"),
                object.optString("removeReason"),
                object.optLong("dateRemoved"),
                object.optString("removedBy"),
                object.has("removeWhen"),
                object.optLong("length")
        };
        
        return DatabaseInsert.create().put(values);
    }
    
    @Override
    public void putPunishment(UUID punishmentId, JSONObject object) {
        try {
            
            Object[] values = new Object[] {
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
            };
            
            if (! doesPunishmentExist(punishmentId)) {
                DatabaseInsert.create().put(values).execute(this);
            } else {
                DatabaseUpdate.create().checkColumns("id").checkValues(punishmentId.toString())
                        .updateColumns("reason", "offender", "moderator", "dateCreated", "type", "id", "modip", "removeReason", "dateRemoved", "removedBy", "scheduledRemove", "length")
                        .updateValues(values).execute(this);
            }
            
            putCache(punishmentId, object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean doesPunishmentExist(UUID punishmentId) {
        try {
            return hasCachedValue(punishmentId) || DatabaseQuery.create().checkColumns("id").checkValues(punishmentId.toString()).execute(this).next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public Set<UUID> getAllPunishmentsIds(UUID offender) {
        try (ResultSet set = DatabaseQuery.create().checkColumns("offender").checkValues(offender.toString()).selectColumns("id").execute(this)) {
            
            Set<UUID> ids = new HashSet<>();
            
            while (set.next())
                ids.add(UUID.fromString(set.getString("id")));
            
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return new HashSet<>();
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
