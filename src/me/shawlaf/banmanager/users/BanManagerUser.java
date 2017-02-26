package me.shawlaf.banmanager.users;

import me.shawlaf.banmanager.managers.database.DatabaseEntry;
import me.shawlaf.banmanager.punish.Punishment;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Florian on 29.12.2016.
 */
public interface BanmanagerUser extends ProxiedPlayer, DatabaseEntry {
    
    Set<UUID> findAlternateAccountIds();
    
    void addIp(String ip);
    
    boolean hasSimilarIps(UUID otherUser);
    
    void purgePunishments();
    
    void setAdmin(boolean state);
    
    boolean isAdmin();
    
    void addPunishment();
    
    Punishment getCurrentBan();
    
    Punishment getCurrentMute();
    
    UUID[] getAllPunishmentIds();
    
}
