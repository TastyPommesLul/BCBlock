package net.tastypommeslul.bCBlock;

import net.tastypommeslul.bCBlock.commands.BCReloadCommand;
import net.tastypommeslul.bCBlock.enums.PunishType;
import net.tastypommeslul.bCBlock.listeners.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class BCBlock extends JavaPlugin {

    private final List<String> defaultWords = List.of("nigger", "n1gger", "nigg3r", "n1gg3r", "faggot", "f4ggot", "fagot", "fag");

    public static List<String> blockedWords;
    public final FileConfiguration config = getConfig();

    public static PunishType punishType;
    public static String banDuration;

    public static String banMessage;
    public static String kickMessage;

    @Override
    public void onEnable() {
        punishType = PunishType.valueOf(config.getString("punish-type", "WARN").toUpperCase());
        banDuration = config.getString("ban-duration", "1h");
        banMessage = config.getString("ban-message", "You have been banned from the server for saying a blocked word.");
        kickMessage = config.getString("kick-message", "You have been kicked from the server saying a blocked word.");

        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        saveConfig();

        if (config.getStringList("blocked-words").isEmpty()) {
            config.set("blocked-words", defaultWords);
            saveConfig();
        }

        blockedWords = config.getStringList("blocked-words");

        Bukkit.getCommandMap().register("bcblock", new BCReloadCommand("reload", "Reloads the blocked words list", "/bcblock reload", List.of("rld")));

    }

    @Override
    public void onDisable() {

    }
}
