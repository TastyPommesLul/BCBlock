package net.tastypommeslul.bCBlock.commands;

import net.tastypommeslul.bCBlock.BCBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BCBAddCommand extends Command {
    private final BCBlock plugin = JavaPlugin.getPlugin(BCBlock.class);

    public BCBAddCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String @NotNull [] args) {
        List<String> exWords = plugin.config.getStringList("blocked-words");
        if (sender.hasPermission("bcblock.add") || sender.hasPermission("bcblock.admin")) {
            if (args.length == 0) {
                sender.sendMessage("Usage: /" + commandLabel + " <word>");
                return true;
            }
            
            String wordToAdd = args[0].toLowerCase();
            
            // Check if the word already exists in the list
            if (exWords.contains(wordToAdd)) {
                sender.sendMessage("The word \"" + wordToAdd + "\" is already in the list.");
                return true;
            }
            
            // Word doesn't exist, add it
            exWords.add(wordToAdd);
            plugin.config.set("blocked-words", exWords);
            BCBlock.blockedWords = plugin.getConfig().getStringList("blocked-words");
            plugin.saveConfig();
            sender.sendMessage("Added blocked word \"" + wordToAdd + "\" to the list.");
            return true;
        }

        return false;
    }
}