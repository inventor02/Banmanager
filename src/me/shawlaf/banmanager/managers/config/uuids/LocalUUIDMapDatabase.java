package me.shawlaf.banmanager.managers.config.uuids;

import me.shawlaf.banmanager.managers.config.AbstractConfiguration;
import me.shawlaf.banmanager.managers.database.UUIDMapDatabase;
import me.shawlaf.banmanager.util.MapUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Florian on 29.12.2016.
 */
public class LocalUUIDMapDatabase extends AbstractConfiguration implements UUIDMapDatabase {
    
    private Map<String, UUID> nameToUUIDMap = new HashMap<>();
    private Map<UUID, String> uuidToNameMap = new HashMap<>();
    
    public LocalUUIDMapDatabase(File file) {
        super(file, Format.CUSTOM);
    }
    
    @Override
    protected void parse(String fileData) {
        for (String line : fileData.split("\n")) {
            nameToUUIDMap.put(line.split(":")[0], UUID.fromString(line.split(":")[1]));
        }
        
        uuidToNameMap = MapUtils.switcheroo(nameToUUIDMap);
    }
    
    @Override
    public void updateNameUUIDSet(String name, UUID uuid) {
        if (checkIfNew(name, uuid)) {
            nameToUUIDMap.put(name, uuid);
            uuidToNameMap.put(uuid, name);
        }
    }
    
    private boolean checkIfNew(String name, UUID uuid) {
        return nameToUUIDMap.get(name) == uuid;
    }
    
    @Override
    protected void saveImplementation(BufferedWriter writer) throws Exception {
        for (String key : nameToUUIDMap.keySet()) {
            writer.write(key + ":" + nameToUUIDMap.get(key).toString() + "\n");
        }
    }
    
    @Override
    public UUID getUUID(String name) {
        return nameToUUIDMap.get(name);
    }
    
    @Override
    public String getName(UUID uuid) {
        return uuidToNameMap.get(uuid);
    }
}
