package me.shawlaf.banmanager.implementation.punish;

import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.punish.Punishment;
import me.shawlaf.banmanager.punish.PunishmentType;
import me.shawlaf.banmanager.users.BanmanagerUser;
import net.md_5.bungee.api.ProxyServer;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Florian on 01.01.2017.
 */
public class CraftPunishment implements Punishment {
    
    private Banmanager plugin;
    private PunishmentType type;
    private UUID offenderUUID, moderatorUUID, removedByUUID, removeWhenModerator, punishmentId;
    private String moderatorIP, reason, removeReason, removeWhenReason;
    private long dateExpire, dateRemoved, removeWhenDate, dateCreated;
    
    public static CraftPunishment createNew(Banmanager plugin, UUID offenderUUID, PunishmentType type, UUID moderatorUUID, String moderatorIP, String reason, long length) {
        return new CraftPunishment(plugin, offenderUUID, type, moderatorUUID, moderatorIP, reason, length);
    }
    
    public static CraftPunishment loadFromDatabase(Banmanager plugin, UUID punishmentId) {
        JSONObject object = plugin.getDatabaseManager().getPunishmentDatabase().getPunishmentObject(punishmentId);
        
        return new CraftPunishment(
                plugin,
                PunishmentType.values()[object.getInt("type")],
                
                object.getUUID("offender"),
                object.getUUID("moderator"),
                object.optUUID("removedBy"),
                object.optJSONObjectNotNull("removeWhen").optUUID("removedBy"),
                punishmentId,
                
                object.getString("modip"),
                object.getString("reason"),
                object.optString("removeReason"),
                object.optJSONObjectNotNull("removeWhen").optString("removeReason"),
                
                object.getLong("length") == - 1L ? - 1L : object.getLong("dateCreated") + object.getLong("length"),
                object.optLong("dateRemoved"),
                object.optJSONObjectNotNull("removeWhen").optLong("dateRemoved"),
                object.getLong("dateCreated")
        );
    }
    
    private CraftPunishment(
            Banmanager plugin,
            PunishmentType type,
            
            UUID offenderUUID,
            UUID moderatorUUID,
            UUID removedByUUID,
            UUID removeWhenModerator,
            UUID punishmentId,
            
            String moderatorIP,
            String reason,
            String removeReason,
            String removeWhenReason,
            
            long dateExpire,
            long dateRemoved,
            long removeWhenDate,
            long dateCreated) {
        this.plugin = plugin;
        this.type = type;
        this.offenderUUID = offenderUUID;
        this.moderatorUUID = moderatorUUID;
        this.removedByUUID = removedByUUID;
        this.removeWhenModerator = removeWhenModerator;
        this.punishmentId = punishmentId;
        this.moderatorIP = moderatorIP;
        this.reason = reason;
        this.removeReason = removeReason;
        this.removeWhenReason = removeWhenReason;
        this.dateExpire = dateExpire;
        this.dateRemoved = dateRemoved;
        this.removeWhenDate = removeWhenDate;
        this.dateCreated = dateCreated;
    }
    
    private CraftPunishment(Banmanager plugin, UUID offenderUUID, PunishmentType type, UUID moderatorUUID, String moderatorIP, String reason, long length) {
        this.plugin = plugin;
        this.type = type;
        this.offenderUUID = offenderUUID;
        this.moderatorUUID = moderatorUUID;
        this.punishmentId = UUID.randomUUID();
        this.moderatorIP = moderatorIP;
        this.reason = reason;
        this.dateCreated = System.currentTimeMillis();
        
        if (length != - 1 && type.hasLength()) {
            dateExpire = dateCreated + length;
        } else if (length == - 1 && type.hasLength()) {
            dateExpire = - 1;
        } else {
            dateExpire = 0;
        }
        
        save();
    }
    
    @Override
    public Map<String, Object> map() {
        
        Map<String, Object> map = new HashMap<>();
        
        map.put("reason", getReason());
        map.put("offender", getOffenderUUID().toString());
        map.put("moderator", moderatorUUID == null ? "CONSOLE" : moderatorUUID.toString());
        map.put("dateCreated", getDateCreated());
        map.put("type", getTypeId());
        map.put("id", getPunishmentId().toString());
        map.put("modIP", getModeratorIP());
        
        if (wasRemoved()) {
            map.put("removeReason", getRemoveReason());
            map.put("dateRemoved", getDateRemoved());
            map.put("removedBy", getRemovedBy() == null ? "CONSOLE" : getRemovedBy().toString());
        }
        
        if (willBeRemoved()) {
            Map<String, Object> removeWhen = new HashMap<>();
            
            removeWhen.put("removeReason", getRemoveWhenReason());
            removeWhen.put("removedBy", getRemoveWhenModerator() == null ? "CONSOLE" : getRemoveWhenModerator().toString());
            removeWhen.put("removeDate", getRemoveWhenDate());
            
            map.put("removeWhen", removeWhen);
        }
        
        if (! isPermanent()) {
            map.put("length", getLength());
        }
        
        
        return map;
    }
    
    @Override
    public Map<Integer, Object> sqlInsertMap() {
        return null; // Not used
    }
    
    @Override
    public long getDateExpire() {
        return dateExpire;
    }
    
    @Override
    public void removeAtDate(String reason, UUID moderator, long when) {
        this.removeWhenReason = reason;
        this.removeWhenModerator = moderator;
        this.removeWhenDate = when;
        
        update();
        save();
    }
    
    @Override
    public String getModeratorIP() {
        
        if (moderatorIP == null)
            moderatorIP = "Unknown (Punished before IPs were tracked)";
        
        return moderatorIP;
    }
    
    @Override
    public String getReason() {
        return reason;
    }
    
    @Override
    public String getRemoveReason() {
        return removeReason;
    }
    
    @Override
    public UUID getOffenderUUID() {
        return offenderUUID;
    }
    
    @Override
    public UUID getModerator() {
        return moderatorUUID;
    }
    
    @Override
    public UUID getRemovedBy() {
        return removedByUUID;
    }
    
    @Override
    public long getDateRemoved() {
        return dateRemoved;
    }
    
    @Override
    public PunishmentType getType() {
        return type;
    }
    
    @Override
    public long getRemoveWhenDate() {
        return removeWhenDate;
    }
    
    @Override
    public long getDateCreated() {
        return dateCreated;
    }
    
    @Override
    public UUID getPunishmentId() {
        return punishmentId;
    }
    
    @Override
    public boolean update() {
        
        if (willBeRemoved() && System.currentTimeMillis() > removeWhenDate) {
            remove(removeWhenModerator, removeWhenReason, removeWhenDate);
            return true;
        }
        
        if (hasExpired() && ! wasRemoved()) {
            remove(null, "Punishment Expired [Automated]", dateExpire);
            return true;
        }
        
        return false;
    }
    
    @Override
    public void changeReason(String newReason) {
        this.reason = newReason;
        
        update();
        save();
    }
    
    @Override
    public void changeLength(long newLength) {
        this.dateExpire = dateCreated + newLength;
        
        update();
        save();
    }
    
    @Override
    public void remove(UUID removedBy, String reason, long when) {
        
        if (wasRemoved()) // Prevent punishment to be removed twice via punishment purge in CraftBanmanagerUser#purgePunishments
            return;
        
        this.removedByUUID = removedBy;
        this.removeReason = reason;
        this.dateRemoved = when;
        
        if (willBeRemoved()) {
            removeWhenDate = - 1;
            removeWhenModerator = null;
            removeWhenReason = null;
        }
        
        // TODO send mail
        
        update();
        save();
    }
    
    @Override
    public Banmanager getPlugin() {
        return plugin;
    }
    
    @Override
    public UUID getRemoveWhenModerator() {
        return removeWhenModerator;
    }
    
    @Override
    public String getRemoveWhenReason() {
        return removeWhenReason;
    }
    
    @Override
    public void reactivate() {
        if (wasRemoved() && ! hasExpired() && type != PunishmentType.KICK) {
            removedByUUID = null;
            removeReason = null;
            dateRemoved = 0;
            
            BanmanagerUser player = Banmanager.getUser(ProxyServer.getInstance().getPlayer(getOffenderUUID()));
            
            if (player != null && type == PunishmentType.BAN) {
                player.disconnect(generateLoginMessage());
            }
            
            update();
            save();
        }
    }
    
    private void save() {
        plugin.getDatabaseManager().getPunishmentDatabase().putPunishment(this);
    }
    
}
