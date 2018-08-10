package net.dertod2.SimpleUnlock.Commands;

import net.dertod2.SimpleUnlock.Binary.SimpleUnlock;
import net.dertod2.SimpleUnlock.Classes.Unlock;
import net.dertod2.SimpleUnlock.Utils.ChatComponent;
import net.dertod2.SimpleUnlock.Utils.ChatComponent.Click;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class UnlockCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("simpleunlock.commands.unlock") || !(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Dir fehlen die benötigten Rechte um diesen Befehl auszuführen!");
			return true;
		}
		
		PermissionUser permissionUser = PermissionsEx.getUser((Player) sender);
		if (permissionUser.inGroup(SimpleUnlock.getConfiguration().getString("player-group"))) {
			sender.sendMessage(ChatColor.GREEN + "Du bist bereits freigeschaltet!");
			return true;
		}
		
		if (args.length == 1 && args[0].equalsIgnoreCase("repeat")) {
			Unlock unlock = SimpleUnlock.unlockList.get(((Player) sender).getUniqueId());
			if (unlock != null) {
				unlock.clearChat();
				unlock.showQuestion();
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase("answer")) {
			Unlock unlock = SimpleUnlock.unlockList.get(((Player) sender).getUniqueId());
			if (unlock != null && NumberUtils.isNumber(args[1])) {
				unlock.answerQuestion(Integer.parseInt(args[1]));
			}
		} else {
			if (SimpleUnlock.unlockList.containsKey(((Player) sender).getUniqueId())) {
				sender.sendMessage(ChatColor.GREEN + "Du befindest dich bereits in einer Freischaltung!");
				
				new ChatComponent("Klicke ", ChatColor.DARK_GREEN)
				.then("hier", ChatColor.GOLD).click(Click.RunCommand, "/unlock repeat")
				.then(" um die letzte Frage zu wiederholen", ChatColor.DARK_GREEN).to(sender);
			} else {
				Long declineLength = SimpleUnlock.declineList.get(((Player) sender).getUniqueId());
				if (declineLength != null && declineLength > System.currentTimeMillis()) {
					sender.sendMessage(ChatColor.RED + "Du hast die Freischaltung nicht bestanden.");
					sender.sendMessage(ChatColor.RED + "Versuche es erneut in: " + ChatColor.GOLD + (System.currentTimeMillis() - declineLength));
					return true;
				} else {
					SimpleUnlock.declineList.remove(((Player) sender).getUniqueId());
				}
				
				SimpleUnlock.unlockList.put(((Player) sender).getUniqueId(), new Unlock(((Player) sender).getUniqueId()));
				
			}
		}
		
		return true;
	}
}