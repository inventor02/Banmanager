package me.shawlaf.banmanager.gui;

import dev.wolveringer.bungeeutil.item.ItemStack;
import dev.wolveringer.bungeeutil.item.Material;
import dev.wolveringer.bungeeutil.item.meta.SkullMeta;
import me.shawlaf.banmanager.async.Multithreading;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Florian on 27.03.2017.
 */
public class ItemStackBuilder { // TODO MAKE THIS
    
    private ItemStack itemStack;
    private Consumer<ItemStack.Click> clickHandler = c -> {};
    
    private ItemStackBuilder(Material material, Consumer<ItemStack.Click> clickHandler) {
        this.clickHandler = clickHandler;
        
        this.itemStack = new ItemStack(material) {
            @Override
            public void click(Click click) {
                clickHandler.accept(click);
            }
        };
        
    }
    
    private ItemStackBuilder(ItemStack stack) {
        this.itemStack = stack;
    }
    
    private ItemStackBuilder(ItemStackBuilder builder) {
        this(builder.itemStack);
    }
    
    private ItemStackBuilder(Material material, UserInterface userInterface, Consumer<ItemStack.Click> clickHandler) {
        this(material, 1, userInterface, clickHandler);
    }
    
    private ItemStackBuilder(Material material) {
        this(material, 1);
    }
    
    private ItemStackBuilder(Material material, int amount, UserInterface userInterface, Consumer<ItemStack.Click> clickHandler) {
        this(material, amount, 0, userInterface, clickHandler);
    }
    
    private ItemStackBuilder(Material material, int amount) {
        this(material, amount, 0, null);
    }
    
    private ItemStackBuilder(Material material, int amount, int damage, UserInterface userInterface) {
        this(material, amount, damage, userInterface, c -> {});
    }
    
    private ItemStackBuilder(Material material, int amount, int damage, UserInterface userInterface, Consumer<ItemStack.Click> clickHandler) {
        this(new ItemStack(material, amount, (short) damage) {
            @Override
            public void click(Click click) {
                clickHandler.accept(click);
                
                if (userInterface != null && userInterface.updateOnClick) {
                    Multithreading.runAsync(userInterface::update);
                }
            }
        });
        
        this.clickHandler = clickHandler;
    }
    
    public static ItemStackBuilder build(Material material) {
        return new ItemStackBuilder(material);
    }
    
    public static ItemStackBuilder build(Material material, UserInterface userInterface, Consumer<ItemStack.Click> clickHandler) {
        return new ItemStackBuilder(material, userInterface, clickHandler);
    }
    
    public static ItemStack buildStack(Material material) {
        return build(material).build();
    }
    
    public static ItemStack buildStack(Material material, UserInterface userInterface, Consumer<ItemStack.Click> clickHandler) {
        return build(material, userInterface, clickHandler).build();
    }
    
    public static ItemStackBuilder fromExistingStack(ItemStack stack) {
        return stack != null ? new ItemStackBuilder(stack) : null;
    }
    
    public static ItemStackBuilder build(Material material, Consumer<ItemStack.Click> clickHandler) {
        return new ItemStackBuilder(material, clickHandler);
    }
    
    public static ItemStackBuilder build(Material material, int amount) {
        return new ItemStackBuilder(material, amount);
    }
    
    public static ItemStackBuilder build(Material material, int amount, UserInterface userInterface, Consumer<ItemStack.Click> clickHandler) {
        return new ItemStackBuilder(material, amount, userInterface, clickHandler);
    }
    
    public static ItemStackBuilder build(Material material, int amount, int damage) {
        return new ItemStackBuilder(material, amount, damage, null);
    }
    
    public static ItemStackBuilder build(Material material, int amount, int damage, UserInterface userInterface, Consumer<ItemStack.Click> clickHandler) {
        return new ItemStackBuilder(material, amount, damage, userInterface, clickHandler);
    }
    
    public static ItemStackBuilder makeSkull(UUID uuid) {
        return build(Material.SKULL_ITEM, 1, 3).skullOwner(uuid);
    }
    
    public static ItemStackBuilder makeSkull(UUID uuid, UserInterface userInterface, Consumer<ItemStack.Click> clickHandler) {
        return build(Material.SKULL_ITEM, 1, 3, userInterface, clickHandler).skullOwner(uuid);
    }
    
    public Material getType() {
        return itemStack.getType();
    }
    
    public ItemStackBuilder type(Material material) {
        itemStack.setType(material);
        return this;
    }
    
    public ItemStackBuilder displayName(String displayName) {
        itemStack.getItemMeta().setDisplayName(displayName);
        return this;
    }
    
    public ItemStackBuilder lore(String... lore) {
        itemStack.getItemMeta().setLore(new LinkedList<>(Arrays.asList(lore)));
        return this;
    }
    
    public ItemStackBuilder addLore(String... extraLore) {
        itemStack.getItemMeta().setLore(Stream.concat(itemStack.getItemMeta().getLore().stream(), Arrays.stream(extraLore)).collect(Collectors.toList()));
        return this;
    }
    
    public ItemStackBuilder damage(int damage) {
        itemStack.setDurability((short) damage);
        return this;
    }
    
    public ItemStackBuilder skullOwner(UUID uuid) {
        ((SkullMeta) itemStack.getItemMeta()).setSkin(uuid);
        return this;
    }
    
    public ItemStack build() {
        return copyItemStack(itemStack, clickHandler);
    }
    
    private static ItemStack copyItemStack(ItemStack itemStack, Consumer<ItemStack.Click> clickHandler) {
        ItemStack newStack = new ItemStack(itemStack.getType(), itemStack.getAmount(), itemStack.getDurability()) {
            @Override
            public void click(Click click) {
                if (clickHandler != null)
                    clickHandler.accept(click);
            }
        };
        
        newStack.getItemMeta().setDisplayName(itemStack.getItemMeta().getDisplayName());
        newStack.getItemMeta().setGlow(itemStack.getItemMeta().hasGlow());
        newStack.getItemMeta().setLore(itemStack.getItemMeta().getLore());
        
        return newStack;
    }
    
    public ItemStackBuilder copy() {
        return fromExistingStack(copyItemStack(itemStack, clickHandler));
    }
    
    @Deprecated
    private static ItemStack copyItemStack(ItemStack itemStack) {
        return copyItemStack(itemStack, itemStack::click);
    }
    
}
