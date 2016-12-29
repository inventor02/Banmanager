package me.shawlaf.banmanager.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Florian on 29.12.2016.
 */
public class JSONUtils {
    
    private JSONUtils() {
    }
    
    public static void putInJSON(JSONObject object, String key, Object value) {
        
        if (value instanceof String) {
            object.put(key, (String) value);
        } else if (value instanceof Collection<?>) {
            object.put(key, toJSONArray((Collection<?>) value));
        } else if (value instanceof Map<?, ?>) {
            object.put(key, toJSONObject((Map<?, ?>) value));
        } else if (value instanceof UUID) {
            object.put(key, value.toString());
        } else {
            object.put(key, value);
        }
        
    }
    
    public static void putInJSON(JSONArray array, Object value) {
        
        if (value instanceof String) {
            array.put((String) value);
        } else if (value instanceof Collection<?>) {
            array.put(toJSONArray((Collection<?>) value));
        } else if (value instanceof Map<?, ?>) {
            array.put(toJSONObject((Map<?, ?>) value));
        } else if (value instanceof UUID) {
            array.put(value.toString());
        } else {
            array.put(value);
        }
    }
    
    public static JSONObject toJSONObject(Map<?, ?> map) {
        JSONObject returnJSON = new JSONObject();
        
        for (Map.Entry<?, ?> entry : map.entrySet())
            putInJSON(returnJSON, entry.getKey().toString(), entry.getValue());
        
        return returnJSON;
        
    }
    
    public static JSONArray toJSONArray(Collection<?> collection) {
        
        JSONArray array = new JSONArray();
        
        for (Object object : collection) {
            putInJSON(array, object);
        }
        
        return array;
        
    }
    
    public static Collection<Object> toCollection(JSONArray array) {
        Collection<Object> collection = new ArrayList<>();
        
        for (int i = 0; i < array.length(); i++) {
            Object entry = array.get(i);
            
            if (entry instanceof JSONObject) {
                collection.add(toMap((JSONObject) entry));
            } else if (entry instanceof JSONArray) {
                collection.add(toCollection((JSONArray) entry));
            } else {
                collection.add(entry);
            }
        }
        
        return collection;
    }
    
    public static List<Object> toList(JSONArray array) {
        return new ArrayList<>(toCollection(array));
    }
    
    public static Map<String, Object> toMap(JSONObject object) {
        Map<String, Object> map = new HashMap<>();
        
        String[] fields = JSONObject.getNames(object);
        
        for (String field : fields) {
            Object entry = object.get(field);
            
            if (entry instanceof JSONObject) {
                map.put(field, toMap((JSONObject) entry));
            } else if (entry instanceof JSONArray) {
                map.put(field, toCollection((JSONArray) entry));
            } else {
                map.put(field, entry);
            }
        }
        
        return map;
    }
    
}
