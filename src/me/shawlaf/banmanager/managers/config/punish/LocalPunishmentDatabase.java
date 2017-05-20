package me.shawlaf.banmanager.managers.config.punish;

import me.shawlaf.banmanager.managers.config.AbstractConfiguration;
import me.shawlaf.banmanager.managers.database.PunishmentDatabase;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Florian on 26.03.2017.
 */
public class LocalPunishmentDatabase extends AbstractConfiguration implements PunishmentDatabase {
    
    private JSONObject document;
    
    public LocalPunishmentDatabase(File file) {
        super(file, Format.JSON);
    }
    
    @Override
    public JSONObject getPunishmentObject(UUID punishmentId) {
        return document.optJSONObject(punishmentId.toString());
    }
    
    @Override
    public void wipePunishment(UUID punishmentId) {
        document.remove(punishmentId.toString());
    }
    
    @Override
    public void putPunishment(UUID punishmentId, JSONObject object) {
        document.put(punishmentId.toString(), object);
    }
    
    @Override
    public boolean doesPunishmentExist(UUID punishmentId) {
        return document.has(punishmentId.toString());
    }
    
    @Override
    public Set<UUID> getAllPunishmentsIds(UUID offender) {
        Set<UUID> ids = new HashSet<>();
        JSONObject tmp;
        
        for (String key : document.keySet()) {
            
            if ((tmp = document.getJSONObject(key)).getString("offender").equals(offender.toString())) {
                ids.add(UUID.fromString(tmp.getString("id")));
            }
        }
        
        return ids;
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
