package net.dertod2.SimpleUnlock.Listeners;

import me.lucko.luckperms.api.User;
import net.dertod2.SimpleUnlock.Binary.SimpleUnlock;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        User permissionUser = SimpleUnlock.luckPermsApi.getUser(event.getPlayer().getUniqueId());
        if (permissionUser.inheritsGroup(
            SimpleUnlock.luckPermsApi.getGroup(SimpleUnlock.getConfiguration().getString("guest-group"))
        )) {
            event.getPlayer().sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        User permissionUser = SimpleUnlock.luckPermsApi.getUser(event.getPlayer().getUniqueId());
        if (permissionUser.inheritsGroup(
            SimpleUnlock.luckPermsApi.getGroup(SimpleUnlock.getConfiguration().getString("guest-group"))
        )) {
            event.getPlayer().sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
            event.setCancelled(true);
        }
    }
}
