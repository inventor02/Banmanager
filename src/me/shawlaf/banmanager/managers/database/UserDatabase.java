package me.shawlaf.banmanager.managers.database;

import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.users.BanmanagerUser;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Florian on 29.12.2016.
 */
public interface UserDatabase {
    
    JSONObject getUserObject(UUID uuid);
    
    void updateUser(UUID uuid, JSONObject object);
    
    default void updateUser(BanmanagerUser user) {
        updateUser(user.getUniqueId(), user.toJSONObject());
    }
    
    boolean has(UUID uuid);
    
    default boolean isAdmin(UUID uuid) {
        JSONObject userObject = getUserObject(uuid);
        
        if (userObject == null)
            return false;
        
        return userObject.optBoolean("admin", false);
    }
    
}
