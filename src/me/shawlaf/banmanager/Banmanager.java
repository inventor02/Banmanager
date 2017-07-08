package me.shawlaf.banmanager;

import dev.wolveringer.bungeeutil.AsyncCatcher;
import me.shawlaf.banmanager.async.Multithreading;
import me.shawlaf.banmanager.implementation.users.CraftBanmanagerUser;
import me.shawlaf.banmanager.managers.ErrorManager;
import me.shawlaf.banmanager.managers.config.BanManagerConfiguration;
import me.shawlaf.banmanager.managers.config.ConfigurationManager;
import me.shawlaf.banmanager.managers.database.DatabaseManager;
import me.shawlaf.banmanager.permissions.Task;
import me.shawlaf.banmanager.punish.Punishment;
import me.shawlaf.banmanager.users.BanmanagerUser;
import me.shawlaf.banmanager.users.OfflineBanmanagerUser;
import me.shawlaf.banmanager.util.JSONUtils;
import me.shawlaf.banmanager.util.chat.C;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.logging.Handler;

/**
 * Created by Florian on 29.12.2016.
 */
public class Banmanager extends Plugin {
    
    private final Set<BanmanagerUser> online = new HashSet<>();
    private final Map<UUID, OfflineBanmanagerUser> offlineUserCache = new HashMap<>();
    
    public BanmanagerUser getUser(ProxiedPlayer player) {
        
        if (player == null)
            return null;
        
        for (BanmanagerUser user : online)
            if (user.getUniqueId() == player.getUniqueId())
                return user;
        
        return new CraftBanmanagerUser(player, this);
    }
    
    public OfflineBanmanagerUser getOfflineUser(String name) {
        return getOfflineUser(databaseManager.getUuidMapDatabase().getUUID(name));
    }
    
    public OfflineBanmanagerUser getOfflineUser(UUID uuid) {
        if (offlineUserCache.containsKey(uuid))
            return offlineUserCache.get(uuid);
        
        return create(uuid, databaseManager.getUserDatabase().getUserObject(uuid));
    }
    
    private OfflineBanmanagerUser create(UUID uuid, JSONObject userObject) {
        
        String name = databaseManager.getUuidMapDatabase().getName(uuid);
        
        Set<String> ips = databaseManager.getIpsDatabase().getIPS(uuid), mail;
        Set<UUID> punishments = databaseManager.getPunishmentDatabase().getAllPunishmentsIds(uuid);
        BooleanSupplier admin = () -> userObject.optBoolean("admin");
        
        mail = new HashSet<>();
        JSONUtils.toCollection(userObject.optJSONArrayNotNull("mail")).stream().map(Object::toString).forEach(mail::add);
        
        OfflineBanmanagerUser create = new OfflineBanmanagerUser() {
            @Override
            public void addIp(String ip) {
                ips.add(ip);
                save();
            }
            
            @Override
            public void setAdmin(boolean state) {
                userObject.put("admin", state);
                save();
            }
            
            @Override
            public boolean isAdmin() {
                return admin.getAsBoolean();
            }
            
            @Override
            public void addPunishment(Punishment punishment) {
                punishments.add(punishment.getPunishmentId());
            }
            
            @Override
            public UUID[] getAllPunishmentIds() {
                return punishments.toArray(new UUID[punishments.size()]);
            }
            
            @Override
            public Set<String> getMail() {
                return mail;
            }
            
            @Override
            public String getName() {
                return name;
            }
            
            @Override
            public UUID getUniqueId() {
                return uuid;
            }
            
            @Override
            public Banmanager getPlugin() {
                return Banmanager.this;
            }
        };
        
        offlineUserCache.put(uuid, create);
        return create;
    }
    
    private boolean successfulStartup = false;
    
    private ConfigurationManager configurationManager;
    private ErrorManager errorManager;
    private DatabaseManager databaseManager;
    
    @Override
    public void onLoad() {
        Multithreading.initialize(this);
        
        if (! ensureBungeeUtil()) {
            getLogger().severe(C.RED + "-------------------------------------------------------------------------------------------------------------------------");
            getLogger().severe(C.RED + "	[Shawlaf's Banmanager] BUNGEEUTIL DEPENDENCY NOT FOUND!");
            getLogger().severe(C.RED + "   PLEASE DOWNLOAD BUNGEEUTIL AT https://www.spigotmc.org/resources/8699");
            getLogger().severe(C.RED + "-------------------------------------------------------------------------------------------------------------------------");
            
            getProxy().getScheduler().schedule(this, this::disable, 1L, TimeUnit.SECONDS);
            return;
        }
        
        AsyncCatcher.disable(this);
        
        this.configurationManager = new ConfigurationManager(this);
        
        if (! configurationManager.loadConfiguration()) {
            getLogger().severe(C.RED + "-------------------------------------------------------------------------------------------------------------------------");
            getLogger().severe(C.RED + "	[Shawlaf's Banmanager] FAILED TO LOAD CONFIGURATION FILES");
            getLogger().severe(C.RED + "-------------------------------------------------------------------------------------------------------------------------");
            
            getProxy().getScheduler().schedule(this, this::disable, 1L, TimeUnit.SECONDS);
            return;
        }
        
        this.errorManager = new ErrorManager(this);
        this.databaseManager = new DatabaseManager(this);
        
        ProxyServer.getInstance().getScheduler().schedule(
                this,
                offlineUserCache::clear,
                configurationManager.getConfiguration().getMysqlCacheClear(),
                configurationManager.getConfiguration().getMysqlCacheClear(),
                TimeUnit.MINUTES
        );
        
        successfulStartup = true;
    }
    
    private boolean ensureBungeeUtil() {
        return getProxy().getPluginManager().getPlugin("BungeeUtil") != null;
    }
    
    private void disable() {
        getProxy().getLogger().info("Disabling plugin " + getDescription().getName() + " version " + getDescription().getVersion() + " by " + getDescription().getAuthor());
        
        try {
            onDisable();
            for (Handler handler : getLogger().getHandlers())
                handler.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        
        getProxy().getScheduler().cancel(Banmanager.this);
        getExecutorService().shutdownNow();
        getProxy().getLogger().info("Disabled plugin " + getDescription().getName() + " version " + getDescription().getVersion() + " by " + getDescription().getAuthor());
    }
    
    public BanManagerConfiguration getConfiguration() {
        return configurationManager.getConfiguration();
    }
    
    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public boolean hasPermission(ProxiedPlayer player, Task permission) {
        return player.hasPermission(permission.getPermissionNode());
    }
    
}
