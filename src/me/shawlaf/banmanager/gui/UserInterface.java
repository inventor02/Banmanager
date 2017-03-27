package me.shawlaf.banmanager.gui;

import dev.wolveringer.bungeeutil.inventory.Inventory;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Florian on 27.03.2017.
 */
public class UserInterface { // TODO FINISH THIS
    
    private Set<UUID> canSafeClose = new HashSet<>();
    protected final Inventory inventory;
    protected final String title;
    
    public boolean updateOnClick = false;
    
    public UserInterface(Plugin plugin, String title, int size, boolean updateOnClick) {
        if (size <= 0 || size % 9 != 0) {
            throw new IllegalArgumentException("Inventory size may only be a positive multiple of 9! (Given: " + size + ")");
        }
        
        if (title != null && title.length() > 32) {
            throw new IllegalArgumentException("Inventory title length may not exceed the length of 32! (Given:" + title.length() + ")");
        }
        
        this.updateOnClick = updateOnClick;
        this.title = title;
        this.inventory = new Inventory(size, title, false); // We already checked the size, no need to let BungeeUtil do it again
    }
    
    public void clear() {
        inventory.clear();
    }
    
    public final int lastSlot() {
        return inventory.getSlots() - 1;
    }
    
    public final int nextFreeSlot() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (inventory.getItem(i) == null)
                return i;
        }
        
        return -1;
    }
}
