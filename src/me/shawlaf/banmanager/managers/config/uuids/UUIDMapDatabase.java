package me.shawlaf.banmanager.managers.config.uuids;

import me.shawlaf.banmanager.managers.config.AbstractConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Florian on 29.12.2016.
 */
public class UUIDMapDatabase extends AbstractConfiguration {
    
    private Map<String, UUID> uuidMap = new HashMap<>();
    
    public UUIDMapDatabase(File file, Format format) {
        super(file, format);
    }
    
    @Override
    protected void parse(String fileData) {
        for (String line : fileData.split("\n")) {
            uuidMap.put(line.split(":")[0], UUID.fromString(line.split(":")[1]));
        }
    }
    
    public void newKnowledge(String name, UUID uuid) {
        if (isItReallyNew(name, uuid))
            uuidMap.put(name, uuid);
    }
    
    private boolean isItReallyNew(String name, UUID uuid) {
        return uuidMap.get(name) == uuid;
    }
    
    @Override
    protected void saveImplementation(BufferedWriter writer) throws Exception {
        for (String key : uuidMap.keySet()) {
            writer.write(key + ":" + uuidMap.get(key).toString() + "\n");
        }
    }
}
