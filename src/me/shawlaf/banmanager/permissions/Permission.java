package me.shawlaf.banmanager.permissions;

import me.shawlaf.banmanager.Banmanager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by Florian on 01.01.2017.
 */
public enum Permission {
    BMINFO_USE("banmanager.punish.info.viewbasic", " \nUse the bminfo command"),
    PUNISHMENT_INFO_VIEW_IP("banmanager.punish.info.view_ip", " \nView IP of the staff member\nthat punished someone"),
    PUNISH_USE("banmanager.command.punish", " \nUse the punish command"),
    PLAYER_BAN("banmanager.punish.ban", " \nBan players"),
    PLAYER_MUTE("banmanager.punish.mute", " \nMute players"),
    PLAYER_KICK("banmanager.punish.kick", " \nKick players"),
    PUNISH_PERMANENT("banmanager.punish.permanent", " \nPunish players permanently"),
    PUNISHMENTS_REMOVE_OWN("banmanager.punish.remove.own", " \nRemove punishments created by\nyourself"),
    PUNISHMENTS_REMOVE_OTHER("banmanager.punish.remove.other", " \nRemove punishments created\nby others"),
    PUNISHMENTS_REMOVE_PURGE("banmanager.punish.remove.purge", " \nPurge someones punishments"),
    PUNISHMENT_MODIFY_ALLOWEDCOMMANDS("banmanager.punish.allowedcommands.modify", " \nManage allowed command\nfor muted players"),
    PUNISHMENT_MODIOFY_HELPERCAP("banmanager.punish.helpercap.modify", " \nSpecify the max length, players without\nthe permanent permission can punish for"),
    DB_FETCH_MOJANG("banmanager.database.fetchmojang", " \nFetch data of a play from mojang and add a player to the database\nto create punishments"),
    COMMAND_SETCOMMAND("banmanager.command.setcommand", " \nManage command labels"),
    PUNISH_TOGGLE_ADMIN("banmanager.punish.admintoggle", " \nToggle admin mode for players\nadmin players cannot be punished"),
    PUNISH_CHANGE_LENGTH("banmanager.punish.edit.length", " \nChange the length of punishments"),
    PUNISH_CHANGE_REASON("banmanager.punish.edit.reason", " \nChange the reason of punishments"),
    PUNISH_REACTIVATE("banmanager.punish.edit.reactivate", " \nReactivate punishments"),
    MODIFY_PERMISSIONS("banmanager.permissions.modify", " \nModify Permissions");
//    MODIFY_SEVERITIES("punish.severity.modify", "\nModify Severities");
    
    private String permission, description;
    
    private static Banmanager plugin;
    
    Permission(String permission, String description) {
        this.permission = permission;
        this.description = description;
    }
    
    public static void initialize(Banmanager banmanager) {
        plugin = banmanager;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getPermissionNode() {
        return permission;
    }
    
    @SuppressWarnings("deprecation")
    public void broadcast(String message) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
            if (plugin.hasPermission(player, this))
                player.sendMessage(message);
    }
    public static Permission fromNode(String node) {
        for (Permission permission : values()) {
            if (permission.getPermissionNode().equals(node))
                return permission;
        }
        return null;
    }
}
