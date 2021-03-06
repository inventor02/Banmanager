package me.shawlaf.banmanager.gui.punish.root;

import dev.wolveringer.bungeeutil.item.Material;
import me.shawlaf.banmanager.Banmanager;
import me.shawlaf.banmanager.gui.ItemStackBuilder;
import me.shawlaf.banmanager.gui.MenuUtil;
import me.shawlaf.banmanager.gui.UserInterface;
import me.shawlaf.banmanager.permissions.Task;
import me.shawlaf.banmanager.punish.PunishmentBuilder;
import me.shawlaf.banmanager.users.OfflineBanmanagerUser;
import me.shawlaf.banmanager.util.SimpleUUIDFetcher;
import me.shawlaf.banmanager.util.chat.C;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Florian on 29.03.2017.
 */
public class RootPunishUserInterface extends UserInterface {
    
    private static final ItemStackBuilder
            TUTORIAL_BASE = ItemStackBuilder
            .build(Material.REDSTONE_TORCH_ON, click -> {
                click.getPlayer().closeInventory();
                click.getPlayer().sendMessage(
                        new ComponentBuilder("To view the demonstration video, ")
                                .color(ChatColor.GRAY)
                                .append("Click here")
                                .color(ChatColor.WHITE)
                                .bold(true)
                                .event(
                                        new ClickEvent(
                                                ClickEvent.Action.OPEN_URL,
                                                "https://www.youtube.com/watch?v=vlGPBW7XGf8"
                                        )
                                )
                                .create()
                );
            })
            .displayName(C.GRAY + "How to use")
            .lore(
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
                    " ",
                    C.WHITE + C.BOLD + "Still confused? Click here to view a demonstration video."),
            COMPASS = ItemStackBuilder
                    .build(Material.COMPASS)
                    .displayName(C.RESET + "Punishment builder")
                    .lore("",
                            C.GRAY + "This menu is used to create a punishment",
                            C.GRAY + "Select the type of punishment and length in this menu"
                    );
    
    
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
                    C.WHITE + "Click the enchanted golden apple to toggle admin mode for this player.",
                    C.WHITE + "Users marked as admins cannot be punished unless admin permissions are revoked.",
                    plugin.getDatabaseManager().getUserDatabase().isAdmin(plugin.getDatabaseManager().getUuidMapDatabase().getUUID(punishmentBuilder.offender))
                            ? C.RED + "This user is marked as an admin!"
                            : C.WHITE + "This user is not marked as an admin."
            );
        }
        
        putItem(COMPASS, 0);
        putItem(tutorial, 8);
        
        OfflineBanmanagerUser offenderProfile = plugin.getOfflineUser(punishmentBuilder.offender);
        
        if (offenderProfile != null) {
            Set<UUID> alternateAccounts = offenderProfile.findAlternateAccountIds();
            
            if (alternateAccounts.size() > 0 && punishmentBuilder.punishmentType != null && ! punishmentBuilder.punishmentType.isSoft()) {
                ItemStackBuilder alts =
                        ItemStackBuilder.build(Material.SKULL_ITEM, click -> {
                            // TODO open alt view
                        })
                                .damage(3)
                                .skullOwner(offenderProfile.getUniqueId())
                                .displayName(C.RESET + alternateAccounts.size() + " alternate account(s)")
                                .lore(
                                        "",
                                        C.GRAY + punishmentBuilder.multiPunish.size() + " account(s) selected for multi-punish.",
                                        C.GRAY + "Click to view alternate accounts."
                                );
                
                putItem(alts, 2, 3);
            }
            
            if (offenderProfile.isAdmin()) {
                putItem(
                        ItemStackBuilder
                                .build(Material.DIAMOND_SWORD)
                                .displayName(C.GOLD + "This user is exempt from punishment!")
                                .glow(true),
                        3, 5
                );
            }
        }
        
        putCloseButton(2, 4);
        
        putItem(
                ItemStackBuilder
                        .build(Material.SKULL_ITEM)
                        .displayName(C.RESET + punishmentBuilder.offender)
                        .lore(offenderProfile != null ? offenderProfile.getPunishmentLore() : null)
                        .skullOwner(offenderProfile != null ? offenderProfile.getUniqueId() : null),
                2, 5
        );
        
        ItemStackBuilder
                confirmItem = ItemStackBuilder
                .build(Material.WOOL, click -> {
                    if (
                            punishmentBuilder.punishmentType != null
                                    && punishmentBuilder.punishmentType.requiresOnline()
                                    && ProxyServer.getInstance().getPlayer(punishmentBuilder.offender) == null) {
                        return;
                    }
                    
                    if (
                            punishmentBuilder.punishmentType == null
                                    || punishmentBuilder.punishmentLengthBuilder.getSum() == 0) {
                        return;
                    }
                    
                    if (
                            offenderProfile == null
                                    || offenderProfile.isAdmin()) {
                        return;
                    }
                    
                    // TODO open confirm inventory
                })
                .damage(13)
                .displayName(C.GREEN + "Continue"),
                errorItem = ItemStackBuilder
                        .build(Material.STAINED_GLASS_PANE)
                        .damage(14);
        
        List<String> errorLore = new ArrayList<>();
        errorLore.add("");
        
        int posType, posReason, posTime = 0;
        
        if (punishmentBuilder.punishmentType != null && punishmentBuilder.punishmentType.isSoft()) {
            posType = MenuUtil.guiPosition(4, 3);
            posReason = MenuUtil.guiPosition(4, 7);
        } else {
            posType = MenuUtil.guiPosition(4, 2);
            posReason = MenuUtil.guiPosition(4, 5);
            posTime = MenuUtil.guiPosition(4, 8);
        }
        
        if (punishmentBuilder.punishmentType == null) {
            errorLore.add(C.RED + "Please select a punishment type!");
            putItem(errorItem.displayName(C.RED + "^ Please select a punishment type here!"), posType + 9);
        }
        
        if (punishmentBuilder.punishmentLengthBuilder.getSum() == 0 && (punishmentBuilder.punishmentType == null || punishmentBuilder.punishmentType.hasLength())) {
            errorLore.add(C.RED + "Please specify a proper punishment length!");
            putItem(errorItem.displayName(C.RED + "^ Please specify a punishment length here!"), posTime + 9);
        }
        
        if (offenderProfile == null) {
            errorLore.add(C.RED + "This player is not in the database!");
        }
        
        if (offenderProfile != null && offenderProfile.isAdmin()) {
            errorLore.add(C.RED + "This player is exempt from punishment!");
        }
        
        if (punishmentBuilder.punishmentType != null && punishmentBuilder.punishmentType.requiresOnline() && ProxyServer.getInstance().getPlayer(punishmentBuilder.offender) == null) {
            errorLore.add(C.RED + punishmentBuilder.punishmentType.name() + " requires player to be online");
        }
        
        confirmItem.lore(errorLore.stream().toArray(String[]::new));
        
        putItem(
                confirmItem,
                2, 6
        );
        
        if (offenderProfile == null && Task.DB_FETCH_MOJANG.canExecute(moderator)) {
            putItem(
                    ItemStackBuilder
                            .build(Material.BOW, click -> {
                                try {
                                    SimpleUUIDFetcher.NameUUIDSet nameUUIDSet = SimpleUUIDFetcher.getNameUUID(punishmentBuilder.offender);
                                    
                                    if (nameUUIDSet != null) {
                                        plugin.getDatabaseManager().getUuidMapDatabase().updateNameUUIDSet(nameUUIDSet.name(), nameUUIDSet.id());
                                        plugin.getDatabaseManager().getUserDatabase().createUser(nameUUIDSet.name(), nameUUIDSet.id());
                                    }
                                } catch (Exception e) {
                                    moderator.sendMessage(C.RED + "Failed to fetch information from Mojang for " + punishmentBuilder.offender + ": " + e.getMessage() + ", make sure the name is properly capitalized!");
                                }
                            })
                            .displayName(C.GOLD + "Fetch from Mojang and add to Database"),
                    2, 7
            );
        }
    }
}
