package me.shawlaf.banmanager.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Florian on 29.12.2016.
 */
public class MapUtils {
    
    private MapUtils() {}
    
    public static <K, V> Map<V, K> switcheroo(Map<K, V> map) {
        Map<V, K> inverted = new HashMap<>();
        
        for (Map.Entry<K, V> entry : map.entrySet())
            inverted.put(entry.getValue(), entry.getKey());
        
        
        return inverted;
    }

}
