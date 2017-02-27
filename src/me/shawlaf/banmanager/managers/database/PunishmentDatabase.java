package me.shawlaf.banmanager.managers.database;

import me.shawlaf.banmanager.punish.Punishment;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Florian on 01.01.2017.
 */
public interface PunishmentDatabase {
    
    JSONObject getPunishmentObject(UUID punishmentId);
    
    void wipePunishment(UUID punishmentId);
    
    void putPunishment(UUID punishmentId, JSONObject object);
    
    boolean doesPunishmentExist(UUID punishmentId);
    
    default void wipePunishment(Punishment punishment) {
        wipePunishment(punishment.getPunishmentId());
    }
    
    default void putPunishment(Punishment punishment) {
        putPunishment(punishment.getPunishmentId(), punishment.toJSONObject());
    }
}
