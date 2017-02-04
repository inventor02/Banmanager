package me.shawlaf.banmanager.implementation.users;

import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.indev.NotYetImplementedException;
import me.shawlaf.banmanager.punish.Punishment;
import me.shawlaf.banmanager.users.BanmanagerUser;
import me.shawlaf.banmanager.util.JSONUtils;
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
        
        throw new NotYetImplementedException();

//        this.implementation = player;
//        this.plugin = plugin;
        
        
    }
    
    private Set<String> fetchKnownIps() {
        throw new NotYetImplementedException();
    }
    
    private Set<UUID> fetchPunishmentIds() {
        throw new NotYetImplementedException();
    }
    
    @Override
    public Map<String, Object> map() {
        throw new NotYetImplementedException();
//        return null; // TODO
    }
    
    @Override
    public Map<Integer, Object> sqlInsertMap() {
        throw new NotYetImplementedException();
//        return null; // TODO
    }
    
    @Override
    public Set<UUID> findAlternateAccountIds() {
        throw new NotYetImplementedException();
//        return null;
    }
    
    @Override
    public void addIp(String ip) {
        throw new NotYetImplementedException();
    }
    
    @Override
    public boolean hasSimilarIps(UUID otherUser) {
        throw new NotYetImplementedException();
//        return false;
    }
    
    @Override
    public void purgePunishments() {
        throw new NotYetImplementedException();
    }
    
    @Override
    public void setAdmin(boolean state) {
        throw new NotYetImplementedException();
    }
    
    @Override
    public boolean isAdmin() {
        throw new NotYetImplementedException();
//        return false;
    }
    
    @Override
    public void addPunishment() {
        throw new NotYetImplementedException();
    }
    
    @Override
    public Punishment getCurrentBan() {
        throw new NotYetImplementedException();
//        return null;
    }
    
    @Override
    public Punishment getCurrentMute() {
        throw new NotYetImplementedException();
//        return null;
    }
    
    @Override
    public UUID[] getAllPunishmentIds() {
        throw new NotYetImplementedException();
//        return new UUID[0];
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
