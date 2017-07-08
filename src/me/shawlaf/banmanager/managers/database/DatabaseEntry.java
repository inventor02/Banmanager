package me.shawlaf.banmanager.managers.database;

import me.shawlaf.banmanager.managers.database.util.DatabaseInsert;
import me.shawlaf.banmanager.util.JSONUtils;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Florian on 29.12.2016.
 */
public interface DatabaseEntry {
    
    Map<String, Object> map();
    
    default JSONObject toJSONObject() {
        return JSONUtils.toJSONObject(map());
    }
    
    DatabaseInsert sqlInsert();
    
}
