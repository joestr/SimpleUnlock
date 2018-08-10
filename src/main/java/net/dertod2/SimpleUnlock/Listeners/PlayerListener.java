package net.dertod2.SimpleUnlock.Listeners;

import java.util.List;

import net.dertod2.SimpleUnlock.Binary.SimpleUnlock;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		PermissionUser permissionUser = PermissionsEx.getUser(event.getPlayer());
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			event.getPlayer().sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		PermissionUser permissionUser = PermissionsEx.getUser(event.getPlayer());
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			event.getPlayer().sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		PermissionUser permissionUser = PermissionsEx.getUser(event.getPlayer());
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			event.getPlayer().sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		PermissionUser permissionUser = PermissionsEx.getUser(event.getPlayer());
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			event.getPlayer().sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		PermissionUser permissionUser = PermissionsEx.getUser(event.getPlayer());
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			//event.getPlayer().sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		PermissionUser permissionUser = PermissionsEx.getUser(event.getPlayer());
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			event.getPlayer().sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerJoinEvent event) {
		PermissionUser permissionUser = PermissionsEx.getUser(event.getPlayer());
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			List<String> declineList = SimpleUnlock.getConfiguration().getStringList("welcome-message");
			for (String message : declineList) {
				event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			}
		}
	}
}
