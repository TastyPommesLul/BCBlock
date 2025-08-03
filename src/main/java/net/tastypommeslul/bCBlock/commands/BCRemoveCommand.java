package net.tastypommeslul.bCBlock.commands;

import net.tastypommeslul.bCBlock.BCBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BCRemoveCommand extends Command {
    private final BCBlock plugin = JavaPlugin.getPlugin(BCBlock.class);

    public BCRemoveCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String @NotNull [] args) {
        if (sender.hasPermission("bcblock.remove") || sender.hasPermission("bcblock.admin")) {
            plugin.reloadConfig();
            plugin.config = plugin.getConfig();
            if (args.length == 0) {
                sender.sendMessage("Usage: /" + commandLabel + " <word>");
                return true;
            }
            
            String wordToRemove = args[0].toLowerCase();
            List<String> blockedWords = new ArrayList<>(plugin.config.getStringList("blocked-words").stream().map(String::toLowerCase).toList());
            
            // Check if the word exists in the list
            if (!blockedWords.contains(wordToRemove)) {
                sender.sendMessage("The word \"" + wordToRemove + "\" is not in the blocked words list.");
                return true;
            }
            
            // Remove the word
            blockedWords.remove(wordToRemove);
            plugin.config.set("blocked-words", blockedWords);
            plugin.saveConfig();
            
            // Update the static variable to reflect the changes
            BCBlock.blockedWords = blockedWords;
            
            sender.sendMessage("Removed \"" + wordToRemove + "\" from the blocked words list.");
            return true;
        }
        
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        List<String> completions = new ArrayList<>(List.of());
        List<String> doneCompletions = new ArrayList<>(List.of());

        if (args.length == 1) {
            List<String> blockedWords = plugin.config.getStringList("blocked-words");

            if (sender.hasPermission("bcblock.remove") || sender.hasPermission("bcblock.admin")) {
                for (String word : blockedWords) {
                    if (word.toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(word.toLowerCase());
                    }
                }
            }
        }

        StringUtil.copyPartialMatches(args[0], completions, doneCompletions);

        return doneCompletions;
    }
}