package me.shawlaf.banmanager.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Florian on 09.07.2017.
 */
public class SimpleUUIDFetcher {
    
    public static interface NameUUIDSet {
        String name();
        
        UUID id();
    }
    
    private SimpleUUIDFetcher() {}
    
    public static NameUUIDSet getNameUUID(String name) {
        try {
            URL url = new URL("https://api.mojang.com/profiles/minecraft");
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            connection.getOutputStream().write(new JSONArray(Arrays.asList(name)).toString(0).getBytes());
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            
            JSONArray array = new JSONArray(new JSONTokener(connection.getInputStream()));
            
            JSONObject profile = array.getJSONObject(0);
            
            String
                    realName = profile.getString("name"),
                    id = profile.getString("id");
            
            UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
            
            return new NameUUIDSet() {
                @Override
                public String name() {
                    return realName;
                }
                
                @Override
                public UUID id() {
                    return uuid;
                }
            };
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
