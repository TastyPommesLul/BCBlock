package net.tastypommeslul.bCBlock.commands;

import net.tastypommeslul.bCBlock.BCBlock;
import net.tastypommeslul.bCBlock.enums.PunishType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BCReloadCommand extends Command {

    private final BCBlock plugin = BCBlock.getPlugin(BCBlock.class);

    public BCReloadCommand(@NotNull String name) {
        super(name);
    }

    public BCReloadCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender,
                           @NotNull String commandLabel,
                           @NotNull String @NotNull [] args) {
        if (sender.hasPermission("bcblock.reload") || sender.hasPermission("bcblock.admin")) {
            plugin.reloadConfig();
            plugin.saveConfig();
            BCBlock.blockedWords = plugin.getConfig().getStringList("blocked-words");
            BCBlock.punishType = PunishType.valueOf(plugin.getConfig().getString("punish-type") != null ? plugin.getConfig().getString("punish-type").toUpperCase() : "BAN");
            BCBlock.banDuration = plugin.getConfig().getString("ban-duration", "1h");
            BCBlock.banMessage = plugin.getConfig().getString("ban-message", "You have been banned from the server for saying a blocked word.");
            BCBlock.kickMessage = plugin.getConfig().getString("kick-message", "You have been kicked from the server saying a blocked word.");
            BCBlock.warnMessage = plugin.getConfig().getString("warn-message", "You have been warned for saying a blocked word.");

            sender.sendMessage("Reloaded Config for Better Chat Block");
            return true;
        }

        return false;
    }
}
