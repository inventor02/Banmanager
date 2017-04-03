package me.shawlaf.cmdlibbungee.info;

import net.md_5.bungee.api.CommandSender;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Command {
    
    String name();
    
    String[] aliases() default { };
    
    String permission()
            
            default "";
    
    int requiredArguments()
            
            default 0;
    
    String permissionMessage()
            
            default "\u00A7cYou do not have permission to execute this command";
    
    String usage() default "";
    
    Class<? extends CommandSender>[] canBeUsedBy() default { CommandSender.class };
    
}
