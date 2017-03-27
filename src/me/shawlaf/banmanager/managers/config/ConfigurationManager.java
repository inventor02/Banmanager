package me.shawlaf.banmanager.managers.config;

import com.google.common.io.ByteStreams;
import me.shawlaf.banmanager.Banmanager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by Florian on 29.12.2016.
 */
public class ConfigurationManager {
    
    private File dataFolder, errorsDirectory, configurationFile, uuidMapFile, punishmentsFile, usersFile, ipsFile, oldUUIDMapFile, oldIPMapFile;
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
            
            if (! configurationFile.exists()) {
                configurationFile.createNewFile();
                
                InputStream is = banmanager.getResourceAsStream("config.yml");
                OutputStream os = new FileOutputStream(configurationFile);
                
                ByteStreams.copy(is, os);
                
                os.close();
                is.close();
            }
            
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configurationFile);
            this.banManagerConfiguration = new BanManagerConfiguration(configuration);
            
            if (banManagerConfiguration.isUsingMysql())
                return true; // return early
            
            this.uuidMapFile = new File(dataFolder, "uuids.banmanager");
            this.punishmentsFile = new File(dataFolder, "punishments.json");
            this.usersFile = new File(dataFolder, "users.json");
            this.ipsFile = new File(dataFolder, "ips.banmanager");
            
            this.oldUUIDMapFile = new File(dataFolder, "uuids.json");
            this.oldIPMapFile = new File(dataFolder, "ips.json");
            
            if (oldUUIDMapFile.exists())
                migrateUUIDS(oldUUIDMapFile, uuidMapFile);
            
            if (oldIPMapFile.exists())
                migrateIPS(oldIPMapFile, ipsFile);
            
        } catch (Exception e) {
            return false;
        }
        
        return true;
    }
    
    private void migrateIPS(File oldFile, File newFile) {
        try {
            StringBuilder fileData = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(oldFile));
            
            String line;
            
            while ((line = reader.readLine()) != null)
                fileData.append(line);
            
            JSONObject object = new JSONObject(fileData.toString());
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
            
            for (String key : object.keySet()) {
                JSONArray array = object.getJSONArray(key);
                
                for (int i = 0; i < array.length(); i++) {
                    String ipEntry = array.getString(i);
                    
                    writer.write(key + ":" + ipEntry + "\n");
                }
            }
            
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void migrateUUIDS(File oldFile, File newFile) {
        try {
            StringBuilder fileData = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(oldFile));
            
            String line;
            
            while ((line = reader.readLine()) != null)
                fileData.append(line);
            
            JSONObject object = new JSONObject(fileData.toString());
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
            
            for (String key : object.keySet())
                writer.write(key + ":" + object.getString(key) + "\n");
            
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public BanManagerConfiguration getConfiguration() {
        return banManagerConfiguration;
    }
    
    public File getConfigurationFile() {
        return configurationFile;
    }
    
    public File getErrorsDirectory() {
        return errorsDirectory;
    }
    
    public File getIpsFile() {
        return ipsFile;
    }
    
    public File getPunishmentsFile() {
        return punishmentsFile;
    }
    
    public File getUsersFile() {
        return usersFile;
    }
    
    public File getUUUIDMapFile() {
        return uuidMapFile;
    }
}
