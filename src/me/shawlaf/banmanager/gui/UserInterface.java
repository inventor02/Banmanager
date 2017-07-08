package me.shawlaf.banmanager.gui;

import dev.wolveringer.bungeeutil.inventory.Inventory;
import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.player.Player;
import me.shawlaf.banmanager.Banmanager;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Florian on 27.03.2017.
 */
public class UserInterface {
    
    public InventoryStyle style = new InventoryStyle() {};
    protected final Inventory inventory;
    protected final String title;
    protected final Banmanager plugin;
    
    public boolean updateOnClick = false;
    
    public UserInterface(Banmanager plugin, String title, int size, boolean updateOnClick) {
        if (size <= 0 || size % 9 != 0) {
            throw new IllegalArgumentException("Inventory size may only be a positive multiple of 9! (Given: " + size + ")");
        }
        
        if (title != null && title.length() > 32) {
            throw new IllegalArgumentException("Inventory title length may not exceed the length of 32! (Given:" + title.length() + ")");
        }
        
        this.plugin = plugin;
        this.updateOnClick = updateOnClick;
        this.title = title;
        this.inventory = new Inventory(size, title, false); // We already checked the size, no need to let BungeeUtil do it again
        
        update();
    }
    
    
    public final void update() {
        clear();
        fill();
        updateInventory();
    }
    
    protected void fill() {
        
    }
    
    public void updateInventory() {
        inventory.updateInventory();
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
        
        return - 1;
    }
    
    public final void putItem(ItemStackBuilder builder, int row, int slot) {
        putItem(builder, MenuUtil.guiPosition(row, slot));
    }
    
    public final void putItem(ItemStackBuilder builder, int slot) {
        inventory.setItem(slot, builder.build());
    }
    
    public final void putItem(ItemStack itemStack, int row, int slot) {
        putItem(itemStack, MenuUtil.guiPosition(row, slot));
    }
    
    public final void putItem(ItemStack itemStack, int slot) {
        inventory.setItem(slot, itemStack);
    }
    
    public final void addItem(ItemStackBuilder builder) {
        inventory.addItem(builder.build());
    }
    
    public final void addItem(ItemStack itemStack) {
        inventory.addItem(itemStack);
    }
    
    public void open(Player player) {
        player.openInventory(inventory);
    }
    
    public void putBackButton(UserInterface back) {
        putItem(style.makeBackButton(c -> back.open(c.getPlayer()), this), lastSlot());
    }
    
    public void putBackButton(UserInterface back, int row, int slot) {
        putItem(style.makeBackButton(c -> back.open(c.getPlayer()), this), row, slot);
    }
    
    public void putBackButton(UserInterface back, int slot) {
        putItem(style.makeBackButton(c -> back.open(c.getPlayer()), this), slot);
    }
    
    public void putBackButton(UserInterface back, InventoryStyle style) {
        putItem(style.makeBackButton(c -> back.open(c.getPlayer()), this), lastSlot());
    }
    
    public void putBackButton(UserInterface back, int row, int slot, InventoryStyle style) {
        putItem(style.makeBackButton(c -> back.open(c.getPlayer()), this), row, slot);
    }
    
    public void putBackButton(UserInterface back, int slot, InventoryStyle style) {
        putItem(style.makeBackButton(c -> back.open(c.getPlayer()), this), slot);
    }
    
    public void putBackButton(UserInterface back, Function<Consumer<ItemStack.Click>, ItemStack> makeBackButton) {
        putItem(makeBackButton.apply((c -> back.open(c.getPlayer()))), lastSlot());
    }
    
    public void putBackButton(UserInterface back, int row, int slot, Function<Consumer<ItemStack.Click>, ItemStack> makeBackButton) {
        putItem(makeBackButton.apply((c -> back.open(c.getPlayer()))), row, slot);
    }
    
    public void putBackButton(UserInterface back, int slot, Function<Consumer<ItemStack.Click>, ItemStack> makeBackButton) {
        putItem(makeBackButton.apply((c -> back.open(c.getPlayer()))), slot);
    }
    
    public void putCloseButton() {
        putItem(style.makeCloseButton(c -> c.getPlayer().closeInventory(), this), lastSlot());
    }
    
    public void putCloseButton(int row, int slot) {
        putItem(style.makeCloseButton(c -> c.getPlayer().closeInventory(), this), row, slot);
    }
    
    public void putCloseButton(int slot) {
        putItem(style.makeCloseButton(c -> c.getPlayer().closeInventory(), this), slot);
    }
    
    public void putCloseButton(InventoryStyle style) {
        putItem(style.makeCloseButton(c -> c.getPlayer().closeInventory(), this), lastSlot());
    }
    
    public void putCloseButton(int row, int slot, InventoryStyle style) {
        putItem(style.makeCloseButton(c -> c.getPlayer().closeInventory(), this), row, slot);
    }
    
    public void putCloseButton(int slot, InventoryStyle style) {
        putItem(style.makeCloseButton(c -> c.getPlayer().closeInventory(), this), slot);
    }
    
    public void putCloseButton(Function<Consumer<ItemStack.Click>, ItemStack> makeCloseButton) {
        putItem(makeCloseButton.apply((c -> c.getPlayer().closeInventory())), lastSlot());
    }
    
    public void putCloseButton(int row, int slot, Function<Consumer<ItemStack.Click>, ItemStack> makeCloseButton) {
        putItem(makeCloseButton.apply((c -> c.getPlayer().closeInventory())), row, slot);
    }
    
    public void putCloseButton(int slot, Function<Consumer<ItemStack.Click>, ItemStack> makeCloseButton) {
        putItem(makeCloseButton.apply((c -> c.getPlayer().closeInventory())), slot);
    }
    
}
