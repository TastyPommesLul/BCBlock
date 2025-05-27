package net.tastypommeslul.bCBlock.commands;

import net.tastypommeslul.bCBlock.BCBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BCRemoveCommand extends Command {
    private final BCBlock plugin = JavaPlugin.getPlugin(BCBlock.class);

    public BCRemoveCommand(@NotNull String name) {
        super(name);
    }

    public BCRemoveCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String @NotNull [] args) {
        if (sender.hasPermission("bcblock.remove") || sender.hasPermission("bcblock.admin")) {
            if (args.length == 0) {
                sender.sendMessage("Usage: /" + commandLabel + " <word>");
                return true;
            }
            
            String wordToRemove = args[0].toLowerCase();
            List<String> blockedWords = plugin.config.getStringList("blocked-words");
            
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
}