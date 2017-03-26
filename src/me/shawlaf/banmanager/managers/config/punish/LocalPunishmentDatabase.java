package me.shawlaf.banmanager.managers.config.punish;

import me.shawlaf.banmanager.managers.config.AbstractConfiguration;
import me.shawlaf.banmanager.managers.database.PunishmentDatabase;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.lang.annotation.Documented;
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
    protected void parse(String fileData) {
        document = new JSONObject(fileData);
    }
    
    @Override
    protected void saveImplementation(BufferedWriter writer) throws Exception {
        writer.write(document.toString(0));
    }
}
