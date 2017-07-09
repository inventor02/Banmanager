package me.shawlaf.banmanager.managers.database;

import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.users.BanmanagerUser;
import me.shawlaf.banmanager.users.OfflineBanmanagerUser;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Florian on 29.12.2016.
 */
public interface UserDatabase {
    
    JSONObject getUserObject(UUID uuid);
    
    void updateUser(UUID uuid, JSONObject object);
    
    void createUser(String name, UUID uuid);
    
    default void updateUser(OfflineBanmanagerUser user) {
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
