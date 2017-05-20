package me.shawlaf.banmanager.gui.punish.root;

import dev.wolveringer.bungeeutil.item.Material;
import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.gui.ItemStackBuilder;
import me.shawlaf.banmanager.gui.UserInterface;
import me.shawlaf.banmanager.permissions.Task;
import me.shawlaf.banmanager.punish.PunishmentBuilder;
import me.shawlaf.banmanager.util.chat.C;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by Florian on 29.03.2017.
 */
public class RootPunishUserInterface extends UserInterface {
    
    private static final ItemStackBuilder TUTORIAL_BASE = ItemStackBuilder.build(Material.REDSTONE_TORCH_ON).displayName(C.GRAY + "How to use").lore(
            "",
            C.WHITE + "This GUI is used to punish users, in this menu you will select",
            C.WHITE + "the type of punishments and the length",
            " ",
            C.WHITE + "Left click the iron sword to select the type of punishments",
            C.WHITE + "Left click the clock to select the length of the punishments", " ",
            C.WHITE + "Left click the book in the bottom left corner to view all previous punishments",
            C.WHITE + "issued to the player",
            " ",
            C.WHITE + C.BOLD + "If the Player has joined the server before " + C.RESET + C.WHITE + "you can",
            C.WHITE + "Click the skull to the left of the cancel button (red wool) to",
            C.WHITE + "view all recorded alt account of this user.",
            C.WHITE + "From there you can select alt accounts to be selected for multi-punish",
            " ");
    
    
    private PunishmentBuilder punishmentBuilder;
    private ProxiedPlayer moderator;
    
    public RootPunishUserInterface(Banmanager plugin, ProxiedPlayer moderator, PunishmentBuilder builder) {
        super(plugin, "Punish: " + builder.offender, 6 * 9, true);
        
        this.moderator = moderator;
        this.punishmentBuilder = builder;
    }
    
    @Override
    protected void fill() {
        ItemStackBuilder tutorial = TUTORIAL_BASE.copy();
        
        if (plugin.getDatabaseManager().getUuidMapDatabase().hasName(punishmentBuilder.offender) && Task.PUNISH_TOGGLE_ADMIN.canExecute(moderator)) {
            tutorial.addLore("",
                    C.WHITE + "Click the enchanted golden apple to toggle admin mode for this player",
                    C.WHITE + "Users marked as admins cannot be punished unless admin permissions are revoked",
                    plugin.getDatabaseManager().getUserDatabase().isAdmin(plugin.getDatabaseManager().getUuidMapDatabase().getUUID(punishmentBuilder.offender))
                            ? C.GREEN + "This user is marked as an amdin"
                            : C.RED + "This user is not marked as an admin"
            );
            
            
        }
        
        
    }
}
