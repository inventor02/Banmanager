package me.shawlaf.banmanager.gui.punish.root;

import dev.wolveringer.bungeeutil.item.Material;
import me.shawlaf.banmanager.gui.ItemStackBuilder;
import me.shawlaf.banmanager.gui.UserInterface;
import me.shawlaf.banmanager.util.chat.C;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by Florian on 29.03.2017.
 */
public class RootPunishUserInterface extends UserInterface {
    
    private ItemStackBuilder tutorial = ItemStackBuilder.build(Material.REDSTONE_TORCH_ON).displayName(C.GRAY + "How to use").lore(
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
    
    
    public RootPunishUserInterface(Plugin plugin, String title, int size, boolean updateOnClick) {
        super(plugin, title, size, updateOnClick);
    }
    
    @Override
    protected void fill() {
        
        
    }
}
