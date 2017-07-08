package me.shawlaf.banmanager.implementation.users;

import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.implementation.punish.CraftPunishment;
import me.shawlaf.banmanager.punish.Punishment;
import me.shawlaf.banmanager.punish.PunishmentType;
import me.shawlaf.banmanager.users.BanmanagerUser;
import me.shawlaf.banmanager.util.JSONUtils;
import me.shawlaf.banmanager.util.TimeUtils;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Florian on 29.12.2016.
 */
public class CraftBanmanagerUser implements BanmanagerUser {
    
    private ProxiedPlayer implementation;
    private Banmanager plugin;
    
    private Set<UUID> punishmentIds = new HashSet<>();
    private boolean adminStatus;
    private Set<String> knownIPs, mail;
    
    
    public CraftBanmanagerUser(ProxiedPlayer player, Banmanager plugin) {
        JSONObject userObject = plugin.getDatabaseManager().getUserDatabase().getUserObject(player.getUniqueId());
        
        this.implementation = player;
        this.plugin = plugin;
        this.punishmentIds = fetchPunishmentIds();
        this.adminStatus = userObject.optBoolean("admin", false);
        this.knownIPs = fetchKnownIps();
        
        JSONArray mailArray = userObject.optJSONArray("mail");
        
        if (mailArray == null)
            mailArray = new JSONArray();
        
        this.mail = new HashSet<>(JSONUtils.toCollection(mailArray).stream().map(Object::toString).collect(Collectors.toList()));

//        this.implementation = player;
//        this.plugin = plugin;
        
        
    }
    
    private Set<String> fetchKnownIps() {
        return plugin.getDatabaseManager().getIpsDatabase().getIPS(implementation.getUniqueId());
    }
    
    private Set<UUID> fetchPunishmentIds() {
        return plugin.getDatabaseManager().getPunishmentDatabase().getAllPunishmentsIds(implementation.getUniqueId());
    }
    
    @Override
    public void addIp(String ip) {
        knownIPs.add(ip);
        save();
    }
    
    @Override
    public boolean haveSimilarIps(UUID otherUser) {
        for (String otherIP : plugin.getDatabaseManager().getIpsDatabase().getIPS(otherUser))
            for (String ip : knownIPs)
                if (otherIP.equals(ip))
                    return true;
        
        return false;
    }
    
    @Override
    public void purgePunishments() {
        String at = TimeUtils.format(System.currentTimeMillis());
        
        for (UUID uuid : punishmentIds) {
            CraftPunishment.loadFromDatabase(plugin, uuid).remove(null, "Punishment Purge from " + at);
        }
    }
    
    @Override
    public void setAdmin(boolean state) {
        adminStatus = state;
        save();
    }
    
    @Override
    public boolean isAdmin() {
        return adminStatus;
    }
    
    @Override
    public void addPunishment(Punishment punishment) {
        punishmentIds.add(punishment.getPunishmentId());
    }
    
    @Override
    public Punishment getCurrentBan() {
        for (UUID uuid : punishmentIds) {
            Punishment punishment = CraftPunishment.loadFromDatabase(plugin, uuid);
            
            if (punishment.isActive() && punishment.getType() == PunishmentType.BAN)
                return punishment;
        }
        
        return null;
    }
    
    @Override
    public Punishment getCurrentMute() {
        for (UUID uuid : punishmentIds) {
            Punishment punishment = CraftPunishment.loadFromDatabase(plugin, uuid);
            
            if (punishment.isActive() && punishment.getType() == PunishmentType.MUTE)
                return punishment;
        }
        
        return null;
    }
    
    @Override
    public UUID[] getAllPunishmentIds() {
        return punishmentIds.toArray(new UUID[punishmentIds.size()]);
    }
    
    @Override
    public Set<String> getMail() {
        return mail;
    }
    
    @Override
    public String getDisplayName() {
        return implementation.getDisplayName();
    }
    
    @Override
    public void setDisplayName(String displayName) {
        implementation.setDisplayName(displayName);
    }
    
    @Override
    public void sendMessage(ChatMessageType chatMessageType, BaseComponent... baseComponents) {
        implementation.sendMessage(chatMessageType, baseComponents);
    }
    
    @Override
    public void sendMessage(ChatMessageType chatMessageType, BaseComponent baseComponent) {
        implementation.sendMessage(chatMessageType, baseComponent);
    }
    
    @Override
    public void connect(ServerInfo serverInfo) {
        implementation.connect(serverInfo);
    }
    
    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback) {
        implementation.connect(serverInfo, callback);
    }
    
    @Override
    public Server getServer() {
        return implementation.getServer();
    }
    
    @Override
    public int getPing() {
        return implementation.getPing();
    }
    
    @Override
    public void sendData(String s, byte[] bytes) {
        implementation.sendData(s, bytes);
    }
    
    @Override
    public PendingConnection getPendingConnection() {
        return implementation.getPendingConnection();
    }
    
    @Override
    public void chat(String s) {
        implementation.chat(s);
    }
    
    @Override
    public ServerInfo getReconnectServer() {
        return implementation.getReconnectServer();
    }
    
    @Override
    public void setReconnectServer(ServerInfo serverInfo) {
        implementation.setReconnectServer(serverInfo);
    }
    
    @Override
    @Deprecated
    public String getUUID() {
        return implementation.getUUID();
    }
    
    @Override
    public UUID getUniqueId() {
        return implementation.getUniqueId();
    }
    
    @Override
    public Banmanager getPlugin() {
        return null;
    }
    
    @Override
    public Locale getLocale() {
        return implementation.getLocale();
    }
    
    @Override
    public void setTabHeader(BaseComponent upper, BaseComponent lower) {
        implementation.setTabHeader(upper, lower);
    }
    
    @Override
    public void setTabHeader(BaseComponent[] upper, BaseComponent[] lower) {
        implementation.setTabHeader(upper, lower);
    }
    
    @Override
    public void resetTabHeader() {
        implementation.resetTabHeader();
    }
    
    @Override
    public void sendTitle(Title title) {
        implementation.sendTitle(title);
    }
    
    @Override
    public boolean isForgeUser() {
        return implementation.isForgeUser();
    }
    
    @Override
    public Map<String, String> getModList() {
        return implementation.getModList();
    }
    
    @Override
    public String getName() {
        return implementation.getName();
    }
    
    @Override
    public void sendMessage(String message) {
        implementation.sendMessage(message);
    }
    
    @Override
    public void sendMessages(String... strings) {
        implementation.sendMessages(strings);
    }
    
    @Override
    public void sendMessage(BaseComponent... baseComponents) {
        implementation.sendMessage(baseComponents);
    }
    
    @Override
    public void sendMessage(BaseComponent baseComponent) {
        implementation.sendMessage(baseComponent);
    }
    
    @Override
    public Collection<String> getGroups() {
        return implementation.getGroups();
    }
    
    @Override
    public void addGroups(String... strings) {
        implementation.addGroups(strings);
    }
    
    @Override
    public void removeGroups(String... strings) {
        implementation.removeGroups(strings);
    }
    
    @Override
    public boolean hasPermission(String node) {
        return implementation.hasPermission(node);
    }
    
    @Override
    public void setPermission(String node, boolean state) {
        implementation.setPermission(node, state);
    }
    
    @Override
    public Collection<String> getPermissions() {
        return implementation.getPermissions();
    }
    
    @Override
    public InetSocketAddress getAddress() {
        return implementation.getAddress();
    }
    
    @Override
    public void disconnect(String message) {
        implementation.disconnect(message);
    }
    
    @Override
    public void disconnect(BaseComponent... baseComponents) {
        implementation.disconnect(baseComponents);
    }
    
    @Override
    public void disconnect(BaseComponent baseComponent) {
        implementation.disconnect(baseComponent);
    }
    
    @Override
    public boolean isConnected() {
        return implementation.isConnected();
    }
    
    @Override
    public Unsafe unsafe() {
        return implementation.unsafe();
    }
    
}
