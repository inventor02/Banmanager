package me.shawlaf.banmanager.managers.config;

import me.shawlaf.banmanager.Banmanager;

import java.io.File;

/**
 * Created by Florian on 29.12.2016.
 */
public class ConfigurationManager {
    
    private File errorsDirectory;
    
    private Banmanager banmanager;
    
    public ConfigurationManager(Banmanager banmanager) {
        this.banmanager = banmanager;
    }
    
    public boolean loadConfiguration() {
        
        if (! banmanager.getDataFolder().exists())
            banmanager.getDataFolder().mkdir();
        
        errorsDirectory = new File(banmanager.getDataFolder(), "errors");
        
        
        return true;
    }
    
}
