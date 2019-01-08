package net.dertod2.SimpleUnlock.Commands;

import java.util.ArrayList;
import java.util.List;

import net.dertod2.SimpleUnlock.Classes.Question;
import net.dertod2.SimpleUnlock.Utils.StringUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class UnlockAdminCommand implements TabExecutor {

    private final List<String> tabCategoryList = ImmutableList.of("add", "del", "edit", "list");
    private final List<String> tabAddList = ImmutableList.of("question", "answer");
    private final List<String> tabEditList = ImmutableList.of("question", "correctanswer");

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("simpleunlock.commands.unlockadmin")) {
            sender.sendMessage(ChatColor.RED + "Dir fehlen die benötigten Rechte um diesen Befehl auszuführen!");
            return true;
        }

        if (args.length >= 3 && args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("question")) {
            Question question = Question.addQuestion(StringUtils.getString(args, 2, " "));
            if (question != null) {
                sender.sendMessage(ChatColor.DARK_GREEN + "Die Frage '" + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', question.getQuestion()) + ChatColor.DARK_GREEN + "' wurde hinzugefügt!");

                BaseComponent bc = new TextComponent("Klicke hier um eine Antwort hinzuzufügen");

                bc.setColor(net.md_5.bungee.api.ChatColor.GOLD);
                bc.setClickEvent(
                    new ClickEvent(
                        Action.SUGGEST_COMMAND,
                        "/" + label + " add answer " + question.getId() + " "
                    )
                );

                ((Player) sender).spigot().sendMessage(bc);
            } else {
                sender.sendMessage(ChatColor.RED + "Die neue Frage wurde nicht hinzugefügt!");
            }
        } else if (args.length >= 4 && args[0].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("answer")) {
            Question question = Question.getQuestion(Integer.parseInt(args[2]));
            if (question != null) {
                String answer = StringUtils.getString(args, 3, " ");
                question.addAnswer(answer);

                sender.sendMessage(ChatColor.GREEN + "Die Antwort wurde hinzugefügt.");
            } else {
                sender.sendMessage(ChatColor.RED + "Die Frage wurde nicht gefunden!");
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("del")) {
            Question question = Question.getQuestion(Integer.parseInt(args[2]));
            if (question != null) {
                question.delete();
                sender.sendMessage(ChatColor.GREEN + "Die Frage wurde gelöscht!");
            } else {
                sender.sendMessage(ChatColor.RED + "Die Frage wurde nicht gefunden!");
            }
        } else if (args.length >= 4 && args[0].equalsIgnoreCase("edit") && args[1].equalsIgnoreCase("question")) {
            Question question = Question.getQuestion(Integer.parseInt(args[2]));
            if (question != null) {
                question.setQuestion(StringUtils.getString(args, 3, " "));
                sender.sendMessage(ChatColor.GREEN + "Die Frage wurde bearbeitet.");
            } else {
                sender.sendMessage(ChatColor.RED + "Die Frage wurde nicht gefunden!");
            }
        } else if (args.length >= 4 && args[0].equalsIgnoreCase("edit") && args[1].equalsIgnoreCase("correctanswer")) {
            Question question = Question.getQuestion(Integer.parseInt(args[2]));
            if (question != null) {
                question.setCorrectAnswer(Integer.parseInt(args[3]));
                sender.sendMessage(ChatColor.DARK_GREEN + "Die richtige Antwort ist nun '" + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', question.getAnswers().get(question.getCorrectAnswer())) + ChatColor.DARK_GREEN + "'");
            } else {
                sender.sendMessage(ChatColor.RED + "Die Frage wurde nicht gefunden!");
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            List<Question> questionList = Question.getQuestions();

            for (Question question : questionList) {

                BaseComponent bc = new TextComponent();

                BaseComponent frage = new TextComponent("Frage ");
                frage.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);

                BaseComponent frageId = new TextComponent(String.valueOf(question.getId()));
                frageId.setColor(net.md_5.bungee.api.ChatColor.GOLD);
                frageId.setClickEvent(
                    new ClickEvent(
                        Action.RUN_COMMAND,
                        "/" + label + " list " + question.getId()
                    )
                );

                BaseComponent doppelpunkt = new TextComponent(": ");
                frage.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);

                BaseComponent richtigefrage = new TextComponent(ChatColor.translateAlternateColorCodes('&', question.getQuestion()));
                frage.setColor(net.md_5.bungee.api.ChatColor.GOLD);

                bc.addExtra(frage);
                bc.addExtra(frageId);
                bc.addExtra(doppelpunkt);
                bc.addExtra(richtigefrage);

                ((Player) sender).spigot().sendMessage(bc);
            }

        } else if (args.length == 2 && args[0].equalsIgnoreCase("list")) {
            Question question = Question.getQuestion(Integer.parseInt(args[1]));
            if (question != null) {
                List<String> answerList = question.getAnswers();
                for (int i = 0; i < answerList.size(); i++) {
                    String string = answerList.get(i);

                    sender.sendMessage(ChatColor.DARK_GREEN + "Antwort " + ChatColor.GOLD + i + ChatColor.DARK_GREEN + ": " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', string));
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Die Frage wurde nicht gefunden!");
            }
        } else {
            sender.sendMessage(ChatColor.GRAY + "/" + label + ChatColor.RESET + " add question <Question>");
            sender.sendMessage(ChatColor.GRAY + "/" + label + ChatColor.RESET + " add answer <QuestionId> <Answer>");

            sender.sendMessage(ChatColor.GRAY + "/" + label + ChatColor.RESET + " del question <QuestionId>");

            sender.sendMessage(ChatColor.GRAY + "/" + label + ChatColor.RESET + " edit question <QuestionId> <Question>");
            sender.sendMessage(ChatColor.GRAY + "/" + label + ChatColor.RESET + " edit correctanswer <QuestionId> <AnswerId>");

            sender.sendMessage(ChatColor.GRAY + "/" + label + ChatColor.RESET + " list [<QuestionId>]");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], this.tabCategoryList, new ArrayList<String>(this.tabCategoryList.size()));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            return StringUtil.copyPartialMatches(args[1], this.tabAddList, new ArrayList<String>(this.tabAddList.size()));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("del")) {
            return ImmutableList.<String>of("question");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {
            return StringUtil.copyPartialMatches(args[1], this.tabEditList, new ArrayList<String>(this.tabEditList.size()));
        }

        return ImmutableList.of();
    }
}
