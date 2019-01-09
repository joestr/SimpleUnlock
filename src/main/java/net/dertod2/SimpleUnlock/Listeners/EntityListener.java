package net.dertod2.SimpleUnlock.Listeners;

import me.lucko.luckperms.api.User;
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

public class EntityListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player player = this.getAttacker(event.getDamager());
        if (player == null) {
            return;
        }

        User permissionUser = SimpleUnlock.luckPermsApi.getUser(player.getUniqueId());
        if (permissionUser.inheritsGroup(
            SimpleUnlock.luckPermsApi.getGroup(SimpleUnlock.getConfiguration().getString("guest-group"))
        )) {
            player.sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        Player player = this.getAttacker(event.getRemover());
        if (player == null) {
            return;
        }

        User permissionUser = SimpleUnlock.luckPermsApi.getUser(player.getUniqueId());
        if (permissionUser.inheritsGroup(
            SimpleUnlock.luckPermsApi.getGroup(SimpleUnlock.getConfiguration().getString("guest-group"))
        )) {
            player.sendMessage(ChatColor.RED + "Du musst freigeschaltet sein um diese Aktion auszuführen!");
            event.setCancelled(true);
        }
    }

    private Player getAttacker(Entity entity) {
        if (entity instanceof Player) {
            return (Player) entity;
        }

        if (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;
            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        }

        return null;
    }
}
