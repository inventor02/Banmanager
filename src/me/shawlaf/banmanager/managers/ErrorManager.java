package me.shawlaf.banmanager.managers;

import dev.wolveringer.bungeeutil.player.Player;
import me.shawlaf.banmanager.Banmanager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.function.Supplier;

/**
 * Created by Florian on 29.12.2016.
 */
public class ErrorManager {
    
    public enum ErrorHandling {
        DISCONNECT, PRINT_ONLY
    }
    
    private ErrorHandling handling = ErrorHandling.DISCONNECT;
    private Banmanager banmanager;
    
    
    public ErrorManager(Banmanager banmanager) {
        this.banmanager = banmanager;
    }
    
    public void runSafe(Runnable runnable) {
        runSafe(runnable, () -> null);
    }
    
    public void runSafe(Runnable runnable, Supplier<Player> playerSupplier) {
        try {
            runnable.run();
        } catch (Exception e) {
            handleError(e, playerSupplier.get());
        }
    }
    
    public <T extends Throwable> void handleError(T t, ProxiedPlayer relatedPlayer) {
        t.printStackTrace();
        
        if (handling == ErrorHandling.DISCONNECT && relatedPlayer != null && relatedPlayer.isConnected()) {
            ComponentBuilder componentBuilder = new ComponentBuilder(t.getMessage()).color(ChatColor.RED);
            
            for (StackTraceElement stackTraceElement : t.getStackTrace()) {
                componentBuilder.append(stackTraceElement.toString()).color(ChatColor.RED);
            }
            
            relatedPlayer.disconnect(componentBuilder.create());
        }
        
    }
    
    
}
