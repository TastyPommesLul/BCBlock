package net.tastypommeslul.bCBlock;

import net.tastypommeslul.bCBlock.commands.BCAddCommand;
import net.tastypommeslul.bCBlock.commands.BCReloadCommand;
import net.tastypommeslul.bCBlock.commands.BCRemoveCommand;
import net.tastypommeslul.bCBlock.enums.PunishType;
import net.tastypommeslul.bCBlock.listeners.ChatListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class BCBlock extends JavaPlugin {

    // default values
    // TODO: Supply default values for each config value
    private final List<String> defaultWords = List.of("nigger", "n1gger", "nigg3r", "n1gg3r", "faggot", "f4ggot", "fagot", "fag");

    // Config values
    // TODO: Add more configuration and documentation
    // TODO: Add integration for more plugins
    public static List<String> blockedWords;
    public static PunishType punishType;
    public static String banDuration;

    // Messages displayed for each action
    // TODO: Add integration for more plugins
    public static String banMessage;
    public static String kickMessage;
    public static String warnMessage;

    public FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        punishType = PunishType.valueOf(config.getString("punish-type", "KICK").toUpperCase());
        banDuration = config.getString("ban-duration", "1h");
        banMessage = config.getString("ban-message", "You have been banned from the server for saying a blocked word.");
        kickMessage = config.getString("kick-message", "You have been kicked from the server saying a blocked word.");
        warnMessage = config.getString("warn-message", "You have been warned for saying a blocked word.");
        saveConfig();

        getServer().getPluginManager().registerEvents(new ChatListener(), this);


        if (config.getStringList("blocked-words").isEmpty()) {
            config.set("blocked-words", defaultWords);
            saveConfig();
        }
        if (config.getString("punish-type") == null) {
            config.set("punish-type", "KICK");
            saveConfig();
        }
        if (config.getString("ban-duration") == null) {
            config.set("ban-duration", "1h");
            saveConfig();
        }
        if (config.getString("ban-message") == null) {
            config.set("ban-message", "You have been banned from the server for saying a blocked word. '{word}'");
            saveConfig();
        }
        if (config.getString("kick-message") == null) {
            config.set("kick-message", "You have been kicked from the server saying a blocked word. '{word}'");
            saveConfig();
        }
        if (config.getString("warn-message") == null) {
            config.set("warn-message", "You have been warned for saying a blocked word. '{word}'");
            saveConfig();
        }
        reloadConfig();

        blockedWords = config.getStringList("blocked-words");

        Bukkit.getCommandMap().register("bcb", new BCReloadCommand("reload", "Reloads the blocked words list", "/bcb:reload", List.of("rld")));
        Bukkit.getCommandMap().register("bcb", new BCAddCommand("add", "Adds a new Word into the blocked words list", "/bcb:add", List.of()));
        Bukkit.getCommandMap().register("bcb", new BCRemoveCommand("remove", "Removes a Word from the blocked words list", "/bcb:remove", List.of("rm", "del")));
    }
}
