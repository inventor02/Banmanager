package me.shawlaf.banmanager.managers.config.ips;

import me.shawlaf.banmanager.managers.config.AbstractConfiguration;
import me.shawlaf.banmanager.managers.database.IPSDatabase;
import me.shawlaf.banmanager.util.JSONUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Florian on 26.03.2017.
 */
public class LocalIPSDatabase extends AbstractConfiguration implements IPSDatabase {
    
    private JSONObject document;
    
    public LocalIPSDatabase(File file) {
        super(file, Format.JSON);
    }
    
    @Override
    public void putIP(UUID uuid, String ip) {
        document.put(uuid.toString(), document.optJSONArrayNotNull(uuid.toString()).put(ip)); // TODO CHECK FOR DUPLICATES
    }
    
    @Override
    public Set<UUID> getUsersWithIP(String ip) {
        Set<UUID> set = new HashSet<>();
        
        for (String id : document.keySet()) {
            JSONArray array = document.getJSONArray(id);
            
            for (int i = 0; i < array.length(); i++) {
                if (array.get(i).equals(ip)) {
                    set.add(UUID.fromString(id));
                    break;
                }
            }
        }
        
        return set;
    }
    
    @Override
    public Set<String> getIPS(UUID uuid) {
        return JSONUtils.toCollection(document.optJSONArrayNotNull(uuid.toString())).stream().map(Object::toString).collect(Collectors.toSet()); // map(Object::toString) is redundant. However, it gets rid of warnings
    }
    
    @Override
    public boolean has(UUID uuid) throws SQLException {
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
