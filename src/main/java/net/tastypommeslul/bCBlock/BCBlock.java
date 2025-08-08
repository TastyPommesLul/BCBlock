package net.tastypommeslul.bCBlock;

import net.tastypommeslul.bCBlock.commands.BCBAddCommand;
import net.tastypommeslul.bCBlock.commands.BCBReloadCommand;
import net.tastypommeslul.bCBlock.commands.BCBRemoveCommand;
import net.tastypommeslul.bCBlock.enums.ForwardType;
import net.tastypommeslul.bCBlock.enums.PunishType;
import net.tastypommeslul.bCBlock.listeners.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class BCBlock extends JavaPlugin {
    public FileConfiguration config = getConfig();

    // default values
    // TODO: Supply default values for each config value
    private final List<String> defaultWords = List.of("nigger", "n1gger", "nigg3r", "n1gg3r", "faggot", "f4ggot", "fagot", "fag");

    // Config values
    // TODO: Add more configuration and documentation
    // TODO: Add integration for more plugins
    public static List<String> blockedWords;
    public static PunishType punishType;
    public static ForwardType forwardType;
    public static String banDuration;
    public static String muteDuration;

    // Messages displayed for each action
    // TODO: Add integration for more plugins
    public static String banMessage;
    public static String kickMessage;
    public static String warnMessage;
    public static String muteMessage;

    @Override
    public void onEnable() {
        reloadConfig();
        punishType = PunishType.valueOf(config.getString("punish-type", "KICK").toUpperCase());
        forwardType = ForwardType.valueOf(config.getString("forward-type", "WORD").toUpperCase());
        banDuration = config.getString("ban-duration", "1h");
        muteDuration = config.getString("mute-duration", "10m");
        banMessage = config.getString("ban-message", "You have been banned from the server for saying a blocked word.");
        kickMessage = config.getString("kick-message", "You have been kicked from the server saying a blocked word.");
        warnMessage = config.getString("warn-message", "You have been warned for saying a blocked word.");
        muteMessage = config.getString("mute-message", "You have been muted for saying a blocked word.");

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);


        if (config.getStringList("blocked-words").isEmpty()) {
            config.set("blocked-words", defaultWords);
        }
        if (config.getString("punish-type") == null) {
            config.set("punish-type", "KICK");
        }
        if (config.getString("forward-type") == null) {
            config.set("forward-type", "WORD");
        }
        if (config.getString("ban-duration") == null) {
            config.set("ban-duration", "1h");
        }
        if (config.getString("mute-duration") == null) {
            config.set("mute-duration", "10m");
        }
        if (config.getString("ban-message") == null) {
            config.set("ban-message", "You have been banned from the server for saying a blocked word. '{word}'");
        }
        if (config.getString("kick-message") == null) {
            config.set("kick-message", "You have been kicked from the server saying a blocked word. '{word}'");
        }
        if (config.getString("warn-message") == null) {
            config.set("warn-message", "You have been warned for saying a blocked word. '{word}'");
        }
        if (config.getString("mute-message") == null) {
            config.set("mute-message", "You have been muted for saying a blocked word. '{word}'");
        }
        saveConfig();

        blockedWords = config.getStringList("blocked-words");

        Bukkit.getCommandMap().register("bcblock", new BCBReloadCommand("reload", "Reloads the blocked words list", "/bcblock:reload", List.of("rld")));
        Bukkit.getCommandMap().register("bcblock", new BCBAddCommand("add", "Adds a new Word into the blocked words list", "/bcblock:add", List.of()));
        Bukkit.getCommandMap().register("bcblock", new BCBRemoveCommand("remove", "Removes a Word from the blocked words list", "/bcblock:remove", List.of("rm", "del")));
    }
}
