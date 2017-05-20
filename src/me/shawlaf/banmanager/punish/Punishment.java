package me.shawlaf.banmanager.punish;

import dev.wolveringer.bungeeutil.item.Material;
import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.managers.database.DatabaseEntry;
import me.shawlaf.banmanager.permissions.Task;
import me.shawlaf.banmanager.users.BanmanagerUser;
import me.shawlaf.banmanager.util.TimeUtils;
import me.shawlaf.banmanager.util.chat.C;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Florian on 29.12.2016.
 */
@Deprecated // TODO Finish Implementation
public interface Punishment extends DatabaseEntry {
    
    long getDateExpire();
    
    void removeAtDate(String reason, UUID moderator, long when);
    
    String getModeratorIP();
    
    String getReason();
    
    String getRemoveReason();
    
    UUID getPunishmentId();
    
    UUID getOffenderUUID();
    
    UUID getModerator();
    
    UUID getRemovedBy();
    
    long getDateRemoved();
    
    PunishmentType getType();
    
    long getRemoveWhenDate();
    
    long getDateCreated();
    
    boolean update();
    
    void changeReason(String newReason);
    
    void changeLength(long newLength);
    
    void remove(UUID removedBy, String reason, long when);
    
    Banmanager getPlugin();
    
    UUID getRemoveWhenModerator();
    
    String getRemoveWhenReason();
    
    void reactivate();
    
    default boolean hasExpired() {
        return getDateExpire() == - 1L ? false : System.currentTimeMillis() > getDateExpire();
    }
    
    default boolean isActive() {
        return isPermanent() ? ! wasRemoved() : hasExpired() ? false : ! wasRemoved();
    }
    
    default boolean wasRemoved() {
        return getRemoveReason() != null;
    }
    
    default boolean willBeRemoved() {
        return getRemoveWhenReason() != null;
    }
    
    default Material getGuiMaterial() {
        return getType().guiMaterial;
    }
    
    default boolean canRemove(BanmanagerUser banmanagerUser) {
        update();
        
        if (getType() == PunishmentType.KICK)
            return false;
        if (getPlugin().hasPermission(banmanagerUser, Task.PUNISHMENT_INFO_VIEW_IP))
            return ! wasRemoved();
        
        return wasRemoved() ? false : getModerator().equals(banmanagerUser.getUniqueId());
    }
    
    default String[] generateLore(BanmanagerUser banmanagerUser) {
        // TODO
        return null;
    }
    
    default String[] generalLore(BanmanagerUser banmanagerUser) {
        List<String> lore = new ArrayList<String>();
        
        lore.add(C.RED + "=== GENERAL INFO ===");
        lore.add(C.RESET + "Reason: " + C.DARK_GRAY + getReason());
        lore.add(C.RESET + "Moderator: " + C.DARK_GRAY + getPlugin().getDatabaseManager().getUuidMapDatabase().getName(getModerator()));
        if (getPlugin().hasPermission(banmanagerUser, Task.PUNISHMENT_INFO_VIEW_IP)) {
            lore.add(C.RESET + "IP: " + C.DARK_GRAY + getModeratorIP());
        }
        if (getType() != PunishmentType.KICK) {
            if (! isPermanent()) {
                lore.add(C.RESET + "Expires: " + C.DARK_GRAY + (isPermanent() ? "Never" : "In " + TimeUtils.getDurationBreakdown(getLenghtLeft())));
            }
            lore.add(C.RESET + "Length: " + C.DARK_GRAY + (isPermanent() ? "Permanent" : TimeUtils.getDurationBreakdown(getLength())));
        }
        
        if (willBeRemoved()) {
            lore.add(" ");
            lore.add(C.RED + "=== PUNISHMENT WILL BE REMOVED ===");
            lore.add(C.RESET + "By: " + C.DARK_GRAY + getPlugin().getDatabaseManager().getUuidMapDatabase().getName(getRemoveWhenModerator()));
            lore.add(C.RESET + "Reason: " + C.DARK_GRAY + getRemoveWhenReason());
            lore.add(C.RESET + "When: " + C.DARK_GRAY + TimeUtils.format(getRemoveWhenDate()));
        }
        
        if (wasRemoved()) {
            lore.add(" ");
            lore.add(C.RED + "=== REMOVE INFO ===");
            lore.add(C.RESET + "Removed reason: " + C.DARK_GRAY + getRemoveReason());
            lore.add(C.RESET + "Removed By: " + C.DARK_GRAY + getPlugin().getDatabaseManager().getUuidMapDatabase().getName(getRemovedBy()));
            lore.add(C.RESET + "Date removed: " + C.DARK_GRAY + TimeUtils.format(getDateRemoved()));
        }
        
        return lore.toArray(new String[lore.size()]);
    }
    
    default int getTypeId() {
        return getType().ordinal();
    }
    
    default void remove(UUID removedBy, String reason) {
        remove(removedBy, reason, System.currentTimeMillis());
    }
    
    default boolean isPermanent() {
        return getDateExpire() == - 1;
    }
    
    default long getLength() {
        return getDateExpire() - getDateCreated();
    }
    
    default long getLenghtLeft() {
        return isPermanent() ? - 1L : getDateExpire() - System.currentTimeMillis();
    }
    
    default String generateLoginMessage() {
        return C.RED + "You are banned from server\nReason: " + getReason() + "\nLength: " + (isPermanent() ? "Permanent" : TimeUtils.getDurationBreakdown(willBeRemoved() ? getRemoveWhenDate() - System.currentTimeMillis() : getLenghtLeft()));
    }
    
    // TODO CHAT MESSAGE ON MUTE
    
}
