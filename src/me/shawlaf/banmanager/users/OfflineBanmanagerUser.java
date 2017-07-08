package me.shawlaf.banmanager.users;

import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.implementation.punish.CraftPunishment;
import me.shawlaf.banmanager.managers.database.DatabaseEntry;
import me.shawlaf.banmanager.managers.database.util.DatabaseInsert;
import me.shawlaf.banmanager.punish.Punishment;
import me.shawlaf.banmanager.punish.PunishmentType;
import me.shawlaf.banmanager.util.JSONUtils;

import java.util.*;

/**
 * Created by Florian on 08.07.2017.
 */
public interface OfflineBanmanagerUser extends DatabaseEntry {
    
    void addIp(String ip);
    
    void setAdmin(boolean state);
    
    boolean isAdmin();
    
    void addPunishment(Punishment punishment);
    
    UUID[] getAllPunishmentIds();
    
    Set<String> getMail();
    
    String getName();
    
    UUID getUniqueId();
    
    Banmanager getPlugin();
    
    default void save() {
        getPlugin().getDatabaseManager().getUserDatabase().updateUser(getUniqueId(), toJSONObject());
    }
    
    default Map<String, Object> map() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("name", getName());
        map.put("uuid", getUniqueId().toString());
        map.put("admin", isAdmin());
        map.put("mail", JSONUtils.toJSONArray(getMail()));
        
        return map;
    }
    
    default DatabaseInsert sqlInsert() {
        return DatabaseInsert.create().put(getName(), getUniqueId().toString(), isAdmin(), JSONUtils.toJSONArray(getMail()));
    }
    
    default boolean haveSimilarIps(UUID otherUser) {
        Set<String> cachedIpSet = getIps();
        
        for (String otherIP : getPlugin().getDatabaseManager().getIpsDatabase().getIPS(otherUser)) {
            for (String ip : cachedIpSet) {
                if (ip.equals(otherIP))
                    return true;
            }
        }
        
        return false;
    }
    
    default void purgePunishments() {
        getPlugin().getDatabaseManager().getPunishmentDatabase().purgePunishments(getUniqueId());
    }
    
    default Set<UUID> findAlternateAccountIds() {
        Set<UUID> alts = new HashSet<>();
        
        for (String ip : getIps()) {
            alts.addAll(getPlugin().getDatabaseManager().getIpsDatabase().getUsersWithIP(ip));
        }
        
        alts.remove(getUniqueId()); // remove self rather than checking for every entry
        return alts;
    }
    
    default Set<String> getIps() {
        return getPlugin().getDatabaseManager().getIpsDatabase().getIPS(getUniqueId());
    }
    
    default Punishment getCurrentBan() {
        for (UUID punishmentId : getAllPunishmentIds()) {
            Punishment punishment = CraftPunishment.loadFromDatabase(getPlugin(), punishmentId);
            
            if (punishment.isActive() && punishment.getType() == PunishmentType.BAN)
                return punishment;
        }
        
        return null;
    }
    
    default Punishment getCurrentMute() {
        for (UUID punishmentId : getAllPunishmentIds()) {
            Punishment punishment = CraftPunishment.loadFromDatabase(getPlugin(), punishmentId);
            
            if (punishment.isActive() && punishment.getType() == PunishmentType.MUTE)
                return punishment;
        }
        
        return null;
    }
    
}
