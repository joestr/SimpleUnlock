package net.dertod2.SimpleUnlock.Binary;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;

import net.dertod2.SimpleUnlock.Classes.Unlock;
import net.dertod2.SimpleUnlock.Classes.UnlockControl;
import net.dertod2.SimpleUnlock.Commands.UnlockAdminCommand;
import net.dertod2.SimpleUnlock.Commands.UnlockCommand;
import net.dertod2.SimpleUnlock.Listeners.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleUnlock extends JavaPlugin {

    private static SimpleUnlock simpleUnlock;

    // start: joestr - add luckperms api
    public static LuckPermsApi luckPermsApi;
    // end  : joestr

    public static UnlockControl unlockControl;

    public static Map<UUID, Unlock> unlockList = new HashMap<UUID, Unlock>();
    public static Map<UUID, Long> declineList = new HashMap<UUID, Long>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        SimpleUnlock.simpleUnlock = this;

        // start: joestr - get LuckPerms instance
        SimpleUnlock.luckPermsApi = LuckPerms.getApi();
        // end  : joestr

        SimpleUnlock.unlockControl = new UnlockControl();
        SimpleUnlock.unlockControl.load();

        if (getConfig().getBoolean("deny-guest-actions", true)) {
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new BlockListener(), this);
            pluginManager.registerEvents(new EntityListener(), this);
            pluginManager.registerEvents(new PlayerListener(), this);
        }

        getCommand("unlock").setExecutor(new UnlockCommand());
        getCommand("unlockadmin").setExecutor(new UnlockAdminCommand());
    }

    public static FileConfiguration getConfiguration() {
        return SimpleUnlock.simpleUnlock.getConfig();
    }
}
