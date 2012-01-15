/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tal.redstonechips.user;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.tal.redstonechips.RedstoneChips;

/**
 *
 * @author Tal Eisenberg
 */
public class WorldEditHook {
    public static boolean isWorldEditInstalled(RedstoneChips rc) {
        return rc.getServer().getPluginManager().getPlugin("WorldEdit")!=null;
    }

    public static Location[] getWorldEditSelection(Player player, RedstoneChips rc) {
        Plugin worldEdit = rc.getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit == null) {
            return null;
        }

        // get access to WorldEditPlugin.getSelection(Player player);
        Method getSelectionMethod = null;
        for (Method m : worldEdit.getClass().getMethods()) {
            if (m.getName().equals("getSelection") && m.getParameterTypes().length==1 && m.getParameterTypes()[0]==Player.class) {
                getSelectionMethod = m;
            }
        }

        if (getSelectionMethod!=null) {
            try {
                // try to get the current selection.
                Object selection = getSelectionMethod.invoke(worldEdit, player);
                if (selection==null) return null;

                // getting two opposite corners of selection.
                Location[] ret = new Location[2];
                ret[0] = (Location)selection.getClass().getMethod("getMinimumPoint").invoke(selection);
                ret[1] = (Location)selection.getClass().getMethod("getMaximumPoint").invoke(selection);

                return ret;
            } catch (IllegalAccessException ex) {
                rc.log(Level.SEVERE, "While communicating with WorldEdit: " + ex.toString());
            } catch (IllegalArgumentException ex) {
                rc.log(Level.SEVERE, "While communicating with WorldEdit: " + ex.toString());
            } catch (InvocationTargetException ex) {
                rc.log(Level.SEVERE, "While communicating with WorldEdit: " + ex.toString());
            } catch (NoSuchMethodException ex) {
                rc.log(Level.SEVERE, "While communicating with WorldEdit: " + ex.toString());
            }
        }

        return null;
    }    
}
