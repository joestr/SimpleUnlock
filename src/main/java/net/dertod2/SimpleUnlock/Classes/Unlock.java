package net.dertod2.SimpleUnlock.Classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.dertod2.SimpleUnlock.Binary.SimpleUnlock;
import net.dertod2.SimpleUnlock.Utils.ChatComponent;
import net.dertod2.SimpleUnlock.Utils.ChatComponent.Click;
import net.dertod2.SimpleUnlock.Utils.ChatComponent.Hover;
import net.dertod2.SimpleUnlock.Utils.StringUtils;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Unlock {
	private UUID player;
	
	private List<Integer> answeredList = new ArrayList<Integer>();
	private Question currentQuestion;
	
	private int usedChances = 0;
	
	public Unlock(UUID uuid) {
		this.player = uuid;
		
		this.currentQuestion = SimpleUnlock.unlockControl.getRandom(this.answeredList);
		
		Player player = Bukkit.getPlayer(this.player);
		if (player != null) {
			this.clearChat();
			
			player.sendMessage(ChatColor.GOLD + "Wähle die Antworten indem du sie mit der Maus anklickst.");
			player.sendMessage(" ");
			
			this.showQuestion();
		}	
	}
	
	/**
	 * Forces the next Question<br />
	 * This method can finish the activation when all questions are answered correctly
	 */
	public void nextQuestion() {
		if (this.currentQuestion != null) this.answeredList.add(this.currentQuestion.getId());
		this.currentQuestion = SimpleUnlock.unlockControl.getRandom(this.answeredList);
		
		int minimumNeeded = SimpleUnlock.getConfiguration().getInt("min-questions", 0);
		if (this.currentQuestion != null && (minimumNeeded == 0 || this.answeredList.size() < minimumNeeded)) {
			this.clearChat();
			
			if (SimpleUnlock.getConfiguration().getBoolean("reset-chances-each-question")) this.usedChances = 0;			
			this.showQuestion();
		} else {
			PermissionManager permissionManager = PermissionsEx.getPermissionManager();
			
			List<String> addList = SimpleUnlock.getConfiguration().getStringList("group-add");
			List<String> delList = SimpleUnlock.getConfiguration().getStringList("group-del");
			
			PermissionUser permissionUser = permissionManager.getUser(this.player);
			if (permissionUser == null) return;
			
			for (String groupName : delList) permissionUser.removeGroup(groupName);
			for (String groupName : addList) permissionUser.addGroup(groupName);
			
			permissionUser.save();
			PermissionsEx.getPermissionManager().clearUserCache(this.player);
			
			SimpleUnlock.unlockList.remove(this.player);
			
			Player player = Bukkit.getPlayer(this.player);
			if (player == null) return;
			
			this.clearChat();
			
			List<String> acceptList = SimpleUnlock.getConfiguration().getStringList("accept-message");
			for (String message : acceptList) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			}
			
			if (SimpleUnlock.getConfiguration().getBoolean("broadcast-unlock")) {
				Collection<? extends Player> playerList = Bukkit.getOnlinePlayers();
				for (Player onlinePlayer : playerList) {
					if (onlinePlayer.equals(player)) continue;
					onlinePlayer.sendMessage(ChatColor.DARK_GREEN + "Herzlich willkommen " + ChatColor.GOLD + player.getName() + ChatColor.DARK_GREEN + "! Viel Spaß auf dem Server!");
				}
			}
		}
	}
	
	public void showQuestion() {
		if (this.currentQuestion == null) return;
		
		Player player = Bukkit.getPlayer(this.player);
		if (player == null) return;
		
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.currentQuestion.getQuestion()));
		
		List<String> questionAnswers = this.currentQuestion.getAnswers();
		List<ChatComponent> shuffleList = new ArrayList<ChatComponent>(questionAnswers.size());
		
		for (int i = 0; i < questionAnswers.size(); i++) {
			String answer = questionAnswers.get(i);
			
			ChatComponent chatComponent = new ChatComponent("-> ", ChatColor.DARK_GREEN)
			.then(ChatColor.translateAlternateColorCodes('&', answer), ChatColor.GOLD)
				.hover(Hover.ShowText, new ChatComponent("Klicke hier um diese Antwort zu wählen."))
				.click(Click.RunCommand, "/unlock answer " + i);
			
			shuffleList.add(chatComponent);
		}
		
		Collections.shuffle(shuffleList, new Random(System.currentTimeMillis()));
		for (ChatComponent chatComponent : shuffleList) {
			chatComponent.to(player);
		}
	}
	
	public void answerQuestion(int answer) {
		if (this.currentQuestion == null) return;
		
		Player player = Bukkit.getPlayer(this.player);
		if (player == null) return;
		
		if (answer == this.currentQuestion.getCorrectAnswer()) {
			this.nextQuestion();
		} else {
			this.clearChat();
			
			if (SimpleUnlock.getConfiguration().getBoolean("reset-chances-each-question")) {
				if (++this.usedChances >= this.currentQuestion.getMaxChances()) {
					List<String> declineList = SimpleUnlock.getConfiguration().getStringList("decline-message");
					for (String message : declineList) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
					}
					
					long declineLength = StringUtils.getTime(SimpleUnlock.getConfiguration().getString("decline-time"));
					
					SimpleUnlock.declineList.put(this.player, System.currentTimeMillis() + declineLength);
					SimpleUnlock.unlockList.remove(this.player);
					
					player.sendMessage(" ");
					player.sendMessage(ChatColor.RED + "Versuche es erneut in: " + ChatColor.GOLD + StringUtils.timeToString(declineLength));
					
					return;
				}
				
				player.sendMessage(ChatColor.RED + " Die Antwort war falsch, bitte versuche es erneut.");
				player.sendMessage(ChatColor.RED + "Du hast noch " + ChatColor.GOLD + (this.currentQuestion.getMaxChances() - this.usedChances) + ChatColor.RED + " Versuche für diese Frage");				
			} else {
				if (++this.usedChances >= SimpleUnlock.getConfiguration().getInt("max-chances")) {
					List<String> declineList = SimpleUnlock.getConfiguration().getStringList("decline-message");
					for (String message : declineList) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
					}
					
					long declineLength = StringUtils.getTime(SimpleUnlock.getConfiguration().getString("decline-time"));
					
					SimpleUnlock.declineList.put(this.player, System.currentTimeMillis() + declineLength);
					SimpleUnlock.unlockList.remove(this.player);
					
					player.sendMessage(" ");
					player.sendMessage(ChatColor.RED + "Versuche es erneut in: " + ChatColor.GOLD + StringUtils.timeToString(declineLength));
				}
				
				player.sendMessage(ChatColor.RED + " Die Antwort war falsch, bitte versuche es erneut.");
				player.sendMessage(ChatColor.RED + "Du hast noch " + ChatColor.GOLD + (SimpleUnlock.getConfiguration().getInt("max-chances") - this.usedChances) + ChatColor.RED + " Versuche für deine Freischaltung!");				

			}
			
			player.sendMessage(" ");
			
			this.showQuestion();
		}
	}
	
	public void clearChat() {
		Player player = Bukkit.getPlayer(this.player);
		if (player == null) return;
		
		for (int i = 0; i < 30; i++) {
			player.sendMessage(" ");
		}
	}
}