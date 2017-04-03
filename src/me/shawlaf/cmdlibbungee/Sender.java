package me.shawlaf.cmdlibbungee;


import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
import java.util.UUID;

public class Sender implements CommandSender {
    
    private CommandSender sender;
    
    public Sender(CommandSender sender) {
        this.sender = sender;
    }
    
    public CommandSender getSender() {
        return sender;
    }
    
    public boolean isPlayer() {
        return sender instanceof ProxiedPlayer;
    }
    
    public ProxiedPlayer castPlayer() {
        if (isPlayer())
            return (ProxiedPlayer) sender;
        return null;
    }
    
    public void refresh(CommandSender sender) {
        this.sender = sender;
    }
    
    public UUID getUniqueId() {
        return isPlayer() ? castPlayer().getUniqueId() : null;
    }
    
    
    @Override
    public String getName() {
        return sender.getName();
    }
    
    @Override
    public void sendMessage(String s) {
        sender.sendMessage(s);
    }
    
    @Override
    public void sendMessages(String... strings) {
        sender.sendMessages(strings);
    }
    
    @Override
    public void sendMessage(BaseComponent... baseComponents) {
        sender.sendMessage(baseComponents);
    }
    
    @Override
    public void sendMessage(BaseComponent baseComponent) {
        sender.sendMessage(baseComponent);
    }
    
    @Override
    public Collection<String> getGroups() {
        return sender.getGroups();
    }
    
    @Override
    public void addGroups(String... strings) {
        sender.addGroups(strings);
    }
    
    @Override
    public void removeGroups(String... strings) {
        sender.removeGroups(strings);
    }
    
    @Override
    public boolean hasPermission(String s) {
        return sender.hasPermission(s);
    }
    
    @Override
    public void setPermission(String s, boolean b) {
        sender.setPermission(s, b);
    }
    
    @Override
    public Collection<String> getPermissions() {
        return sender.getPermissions();
    }
}
