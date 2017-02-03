package me.shawlaf.banmanager;

import dev.wolveringer.bungeeutil.AsyncCatcher;
import me.shawlaf.banmanager.async.Multithreading;
import me.shawlaf.banmanager.implementation.users.CraftBanmanagerUser;
import me.shawlaf.banmanager.managers.ErrorManager;
import me.shawlaf.banmanager.managers.config.BanManagerConfiguration;
import me.shawlaf.banmanager.managers.config.ConfigurationManager;
import me.shawlaf.banmanager.managers.database.DatabaseManager;
import me.shawlaf.banmanager.users.BanmanagerUser;
import me.shawlaf.banmanager.util.chat.C;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Handler;

/**
 * Created by Florian on 29.12.2016.
 */
public class Banmanager extends Plugin {
    
    private static final Set<BanmanagerUser> online = new HashSet<>();
    private static Supplier<Banmanager> getPlugin = () -> null;
    
    public static BanmanagerUser get(ProxiedPlayer player) {
        
        if (player == null)
            return null;
        
        for (BanmanagerUser user : online)
            if (user.getUniqueId() == player.getUniqueId())
                return user;
        
        return new CraftBanmanagerUser(player, getPlugin.get());
    }
    
    private boolean successfulStartup = false;
    
    private ConfigurationManager configurationManager;
    private ErrorManager errorManager;
    private DatabaseManager databaseManager;
    
    @Override
    public void onLoad() {
        
        getPlugin = () -> this;
        
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
        this.errorManager = new ErrorManager(this);
        this.databaseManager = new DatabaseManager(this);
        
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
    
    public boolean hasPermission(BanmanagerUser banmanagerUser, String punishmentInfoViewIp) {
        return true; // TODO
    }
}
