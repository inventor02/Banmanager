package me.shawlaf.banmanager.managers.config;

import com.google.common.io.ByteStreams;
import me.shawlaf.banmanager.Banmanager;
import net.md_5.bungee.conf.YamlConfig;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Florian on 29.12.2016.
 */
public class ConfigurationManager {
    
    private File dataFolder, errorsDirectory, configurationFile;
    private Banmanager banmanager;
    private Configuration configuration;
    
    private BanManagerConfiguration banManagerConfiguration;
    
    public ConfigurationManager(Banmanager banmanager) {
        this.banmanager = banmanager;
    }
    
    public boolean loadConfiguration() {
        try {
            dataFolder = banmanager.getDataFolder();
    
            if (! dataFolder.exists())
                dataFolder.mkdir();
    
            this.errorsDirectory = new File(dataFolder, "errors");
            this.configurationFile = new File(dataFolder, "config.yml");
            
            if (!configurationFile.exists()) {
                configurationFile.createNewFile();
                
                InputStream is = banmanager.getResourceAsStream("config.yml");
                OutputStream os = new FileOutputStream(configurationFile);
    
                ByteStreams.copy(is, os);
                
                os.close();
                is.close();
            }
            
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configurationFile);
            this.banManagerConfiguration = new BanManagerConfiguration(configuration);
            
            
            
            
            
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }
    
    public BanManagerConfiguration getConfiguration() {
        return banManagerConfiguration;
    }
}
