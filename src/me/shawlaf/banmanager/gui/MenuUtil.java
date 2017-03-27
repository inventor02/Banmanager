package me.shawlaf.banmanager.gui;

/**
 * Created by Florian on 27.03.2017.
 */
public class MenuUtil {
    
    private MenuUtil() {}
    
    public static int guiPosition(int row, int slot) {
        return (9 * (row - 1)) + (slot - 1);
    }
    
}