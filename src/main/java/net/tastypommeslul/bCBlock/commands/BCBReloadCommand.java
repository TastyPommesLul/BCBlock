package net.tastypommeslul.bCBlock.commands;

import net.tastypommeslul.bCBlock.BCBlock;
import net.tastypommeslul.bCBlock.enums.ForwardType;
import net.tastypommeslul.bCBlock.enums.PunishType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BCBReloadCommand extends Command {

    private final BCBlock plugin = BCBlock.getPlugin(BCBlock.class);

    public BCBReloadCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender,
                           @NotNull String commandLabel,
                           @NotNull String @NotNull [] args) {

        if (sender.hasPermission("bcblock.reload") || sender.hasPermission("bcblock.admin")) {
            // First reload the config to get the latest changes from the file
            plugin.reloadConfig();
            plugin.config = plugin.getConfig();
        
            // Now get the latest blocked words list (including manual changes)
            List<String> currentWords = plugin.config.getStringList("blocked-words");
            List<String> uniqueWords = new ArrayList<>();

            // Create a deduplicated list
            int duplicatesRemoved = 0;
            for (String word : currentWords) {
                if (!uniqueWords.contains(word)) {
                    uniqueWords.add(word);
                } else {
                    duplicatesRemoved++;
                }
            }

            // Only save back if there were duplicates removed
            if (duplicatesRemoved > 0) {
                plugin.config.set("blocked-words", uniqueWords);
                plugin.saveConfig();
            }

            // Update the static variables with the current config values
            BCBlock.blockedWords = uniqueWords;
            BCBlock.punishType = PunishType.valueOf(plugin.config.getString("punish-type") != null ? 
                             plugin.config.getString("punish-type").toUpperCase() : "WARN");
            BCBlock.forwardType = ForwardType.valueOf(plugin.config.getString("forward-type") != null ?
                             plugin.config.getString("forward-type").toUpperCase() : "WORD");
            BCBlock.banDuration = plugin.config.getString("ban-duration", "1h");
            BCBlock.muteDuration = plugin.config.getString("mute-duration", "10m");
            BCBlock.banMessage = plugin.config.getString("ban-message");
            BCBlock.kickMessage = plugin.config.getString("kick-message");
            BCBlock.warnMessage = plugin.config.getString("warn-message");
            BCBlock.muteMessage = plugin.config.getString("mute-message");

            // Inform the sender about the operation
            if (duplicatesRemoved > 0) {
                sender.sendMessage("Reloaded Config for Better Chat Block. Removed " + duplicatesRemoved + " duplicate words.");
            } else {
                sender.sendMessage("Reloaded Config for Better Chat Block. No duplicates were found.");
            }
            return true;
        }

        return false;
    }
}