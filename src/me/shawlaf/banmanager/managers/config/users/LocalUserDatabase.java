package me.shawlaf.banmanager.managers.config.users;

import me.shawlaf.banmanager.managers.config.AbstractConfiguration;
import me.shawlaf.banmanager.managers.database.UserDatabase;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.util.UUID;

/**
 * Created by Florian on 29.12.2016.
 */
public class LocalUserDatabase extends AbstractConfiguration implements UserDatabase {
    
    private JSONObject document;
    
    public LocalUserDatabase(File file) {
        super(file, Format.JSON);
    }
    
    @Override
    public JSONObject getUserObject(UUID uuid) {
        return document.getJSONObject(uuid.toString());
    }
    
    @Override
    public void updateUser(UUID uuid, JSONObject object) {
        document.put(uuid.toString(), object);
    }
    
    @Override
    public boolean has(UUID uuid) {
        return document.has(uuid.toString());
    }
    
    @Override
    protected void parse(String fileData) {
        document = new JSONObject(fileData);
    }
    
    @Override
    protected void saveImplementation(BufferedWriter writer) throws Exception {
        writer.write(document.toString(0));
    }
}
