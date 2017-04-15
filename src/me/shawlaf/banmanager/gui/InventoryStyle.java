package me.shawlaf.banmanager.gui;

import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.Material;
import me.shawlaf.banmanager.gui.util.WoolColors;
import me.shawlaf.banmanager.util.chat.C;

import java.util.function.Consumer;

/**
 * Created by Florian on 27.03.2017.
 */
public interface InventoryStyle {
    
    default ItemStack makeBackButton(Consumer<ItemStack.Click> clickHandler, UserInterface userInterface) {
        return ItemStackBuilder.build(Material.ARROW, userInterface, clickHandler).displayName(C.RED + "Back").build();
    }
    
    default ItemStack makeCloseButton(Consumer<ItemStack.Click> clickHandler, UserInterface userInterface) {
        return ItemStackBuilder.build(Material.WOOL, 1, WoolColors.RED, userInterface, clickHandler).displayName("Close").build();
    }
    
    
}
