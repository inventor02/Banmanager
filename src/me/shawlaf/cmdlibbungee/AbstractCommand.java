package me.shawlaf.cmdlibbungee;

import me.shawlaf.cmdlibbungee.info.Command;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.command.ConsoleCommandSender;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractCommand {
    
    private static Map<UUID, Sender> cachedSenders = new HashMap<>();
    private static Sender consoleSender;
    
    private static Sender getSender(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            
            Sender s;
            
            if (cachedSenders.containsKey(player.getUniqueId())) {
                s = cachedSenders.get(player.getUniqueId());
                s.refresh(player);
            } else {
                cachedSenders.put(player.getUniqueId(), s = new Sender(player));
            }
            
            return s;
        }
        if (sender instanceof ConsoleCommandSender)
            return consoleSender == null ? consoleSender = new Sender(sender) : consoleSender;
        return null;
    }
    
    private Command info;
    private Plugin plugin;
    protected boolean asynchronous = false;
    
    public AbstractCommand(Plugin plugin) {
        
        this.plugin = plugin;
        
        if (getClass().isAnnotationPresent(Command.class))
            info = getClass().getAnnotation(Command.class);
        else {
            try {
                
                Method executeMethod = getClass().getMethod("execute", Sender.class, Arguments.class);
                
                if (executeMethod.isAnnotationPresent(Command.class)) {
                    info = executeMethod.getAnnotation(Command.class);
                }
            } catch (Exception exception) {
                System.out.println("Failed to register command with class name " + getClass().getName() + "! Stacktrace:");
                exception.printStackTrace();
                return;
            }
            
        }
        
        if (info == null) {
            throw new RuntimeException("Couldn't find Command annotation in Command class " + getClass().getName());
        }
        
        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new net.md_5.bungee.api.plugin.Command(name(), permission(), aliases()) {
            @Override
            public void execute(CommandSender commandSender, String[] args) {
                handle(commandSender, args);
            }
        });
    }
    
    public String name() {
        return info.name();
    }
    
    public String[] aliases() {
        return info.aliases();
    }
    
    public String permission() {
        return info.permission();
    }
    
    public int requiredArguments() {
        return info.requiredArguments();
    }
    
    public String usage() {
        return info.usage();
    }
    
    public String permissionMessage() {
        return info.permissionMessage();
    }
    
    public String generateUsage() {
        return ChatColor.RED + "Usage: /" + name() + " " + usage();
    }
    
    public final void handle(CommandSender sender, String[] args) {
        
        boolean canUse = false;
        
        for (Class<? extends CommandSender> clazz : info.canBeUsedBy()) {
            if (clazz.isAssignableFrom(sender.getClass())) {
                canUse = true;
                break;
            }
        }
        
        if (! canUse) {
            sender.sendMessage(ChatColor.RED + "You may not use this command");
            return;
        }
        
        if (! sender.hasPermission(permission())) {
            sender.sendMessage(permissionMessage());
            return;
        }
        
        if (args.length < requiredArguments()) {
            sender.sendMessage(generateUsage());
            return;
        }
        
        try {
            Runnable execute = () -> execute(getSender(sender), new Arguments(args));
            
            if (! asynchronous)
                execute.run();
            else
                plugin.getExecutorService().submit(execute);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An Error occured: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    public abstract void execute(Sender sender, Arguments arguments);
    
}
