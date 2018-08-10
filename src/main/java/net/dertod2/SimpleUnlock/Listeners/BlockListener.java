package net.dertod2.SimpleUnlock.Listeners;

import net.dertod2.SimpleUnlock.Binary.SimpleUnlock;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		PermissionUser permissionUser = PermissionsEx.getUser(event.getPlayer());
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			event.getPlayer().sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		PermissionUser permissionUser = PermissionsEx.getUser(event.getPlayer());
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			event.getPlayer().sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
			event.setCancelled(true);
		}
	}
}
