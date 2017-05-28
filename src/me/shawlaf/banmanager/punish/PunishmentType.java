package me.shawlaf.banmanager.punish;

import dev.wolveringer.bungeeutil.item.Material;

/**
 * Created by Florian on 01.01.2017.
 */
public enum PunishmentType {
    
    WARNING(Material.IRON_SWORD), KICK(Material.IRON_DOOR), BAN(Material.BARRIER), MUTE(Material.BOOK_AND_QUILL);
    
    public final Material guiMaterial;
    
    PunishmentType(Material material) {
        this.guiMaterial = material;
    }
    
    public boolean hasLength() {
        return this == BAN || this == MUTE;
    }
    
}
