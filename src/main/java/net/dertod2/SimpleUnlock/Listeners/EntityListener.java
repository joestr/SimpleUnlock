package net.dertod2.SimpleUnlock.Listeners;

import net.dertod2.SimpleUnlock.Binary.SimpleUnlock;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class EntityListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Player player = this.getAttacker(event.getDamager());
		if (player == null) return;
		
		PermissionUser permissionUser = PermissionsEx.getUser(player);
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			player.sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
		Player player = this.getAttacker(event.getRemover());
		if (player == null) return;
		
		PermissionUser permissionUser = PermissionsEx.getUser(player);
		if (!permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			player.sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
			event.setCancelled(true);
		}
	}
	
	private Player getAttacker(Entity entity) {
		if (entity instanceof Player) return (Player) entity;
		
		if (entity instanceof Projectile) {
			Projectile projectile = (Projectile) entity;
			if (projectile.getShooter() instanceof Player) return (Player) projectile.getShooter();
		}
		
		return null;
	}
}
