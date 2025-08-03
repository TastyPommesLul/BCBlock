package net.tastypommeslul.bCBlock.listeners;

import io.papermc.paper.event.player.ChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.tastypommeslul.bCBlock.BCBlock;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;

public class ChatListener implements Listener {
    private final BCBlock plugin;

    public ChatListener(BCBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        plugin.reloadConfig();
        Player p = e.getPlayer();
        if (p.hasPermission("bcblock.bypass") || p.hasPermission("bcblock.admin")) {
            p.sendRichMessage("<bold><green>You have bypass permissions. You can chat freely, no messages will be blocked.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent e) {
        plugin.reloadConfig();
        Component message = e.message();
        TextComponent textMessage = (TextComponent) message;
        Player p = e.getPlayer();

        if (p.hasPermission("bcblock.bypass") || p.hasPermission("bcblock.admin")) {
            e.setCancelled(false);
            return;
        }

        // Split the message into words
        String[] messageWords = textMessage.content().split(" ");


        // Check each word against the blocked words list
        for (String messageWord : messageWords) {
            // Remove all non-alphabetic characters and convert to lowercase
            String filteredWord = messageWord.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

            setPlaceHolder(p, filteredWord);

            // Check if the filtered word matches any blocked word
            for (String blockedWord : BCBlock.blockedWords) {
                if (filteredWord.equals(blockedWord.toLowerCase())) {
                    e.setCancelled(true);


                    switch (BCBlock.punishType) {
                        case KICK:
                            p.kick(Component.text(BCBlock.kickMessage));
                            break;
                        case BAN:
                            p.ban(BCBlock.banMessage,
                                    Duration.parse(BCBlock.banDuration.endsWith("s") ||
                                            BCBlock.banDuration.endsWith("m") ||
                                            BCBlock.banDuration.endsWith("h") ? "PT" + BCBlock.banDuration : "P" + BCBlock.banDuration), "[BCBlock]");
                            break;
                        case WARN:
                            BCBlock.getPlugin(BCBlock.class).getServer().dispatchCommand(BCBlock.getPlugin(BCBlock.class).getServer().getConsoleSender(), "warn " + p.getName() + " " + BCBlock.warnMessage);
                            break;
                        case MUTE:
                            BCBlock.getPlugin(BCBlock.class).getServer().dispatchCommand(BCBlock.getPlugin(BCBlock.class).getServer().getConsoleSender(), "mute " + p.getName() + " " + BCBlock.muteDuration + " " + BCBlock.muteMessage);
                            break;
                        case NOTIFY:
                            p.sendRichMessage("Your message has been blocked! <hover:show_text:'The Word <red><bold>" + filteredWord + "</bold></red> is Blocked on this Server!'>[hover this for info]</hover>");
                            if (BCBlock.playerForwarding) {
                                p.sendMessage("This will be forwarded to the administrators of this server.");
                            }
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.hasPermission("bcblock.notify") || player.hasPermission("bcblock.admin")) {
                                    player.sendRichMessage(p.getName() + "'s message contained a blocked word '<red><bold>" + filteredWord + "</bold></red>'.");
                                }
                            }
                            break;
                        default:
                            p.sendMessage(Component.text(BCBlock.warnMessage));
                            break;
                    }
                    resetPlaceHolder(p, filteredWord);
                    return;
                }
            }
        }
    }

    private void setPlaceHolder(Player p, String filteredWord) {

        // {word} replacement
        BCBlock.warnMessage = BCBlock.warnMessage.replace("{word}", filteredWord);
        BCBlock.banMessage = BCBlock.banMessage.replace("{word}", filteredWord);
        BCBlock.kickMessage = BCBlock.kickMessage.replace("{word}", filteredWord);
        BCBlock.muteMessage = BCBlock.muteMessage.replace("{word}", filteredWord);
        // {player} replacement
        BCBlock.warnMessage = BCBlock.warnMessage.replace("{player}", p.getName());
        BCBlock.banMessage = BCBlock.banMessage.replace("{player}", p.getName());
        BCBlock.kickMessage = BCBlock.kickMessage.replace("{player}", p.getName());
        BCBlock.muteMessage = BCBlock.muteMessage.replace("{player}", p.getName());
        // {banDuration} replacement
        BCBlock.warnMessage = BCBlock.warnMessage.replace("{banDuration}", BCBlock.banDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"));
        BCBlock.banMessage = BCBlock.banMessage.replace("{banDuration}", BCBlock.banDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"));
        BCBlock.kickMessage = BCBlock.kickMessage.replace("{banDuration}", BCBlock.banDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"));
        BCBlock.muteMessage = BCBlock.muteMessage.replace("{banDuration}", BCBlock.banDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"));
        // {muteDuration} replacement
        BCBlock.warnMessage = BCBlock.warnMessage.replace("{muteDuration}", BCBlock.muteDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"));
        BCBlock.banMessage = BCBlock.banMessage.replace("{muteDuration}", BCBlock.muteDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"));
        BCBlock.kickMessage = BCBlock.kickMessage.replace("{muteDuration}", BCBlock.muteDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"));
        BCBlock.muteMessage = BCBlock.muteMessage.replace("{muteDuration}", BCBlock.muteDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"));
    }

    private void resetPlaceHolder(Player p, String filteredWord) {
        // {word} replacement
        BCBlock.warnMessage = BCBlock.warnMessage.replace(filteredWord, "{word}");
        BCBlock.banMessage = BCBlock.banMessage.replace(filteredWord, "{word}");
        BCBlock.kickMessage = BCBlock.kickMessage.replace(filteredWord, "{word}");
        BCBlock.muteMessage = BCBlock.muteMessage.replace(filteredWord, "{word}");
        // {player} replacement
        BCBlock.warnMessage = BCBlock.warnMessage.replace(p.getName(), "{player}");
        BCBlock.banMessage = BCBlock.banMessage.replace(p.getName(), "{player}");
        BCBlock.kickMessage = BCBlock.kickMessage.replace(p.getName(), "{player}");
        BCBlock.muteMessage = BCBlock.muteMessage.replace(p.getName(), "{player}");
        // {banDuration} replacement
        BCBlock.warnMessage = BCBlock.warnMessage.replace(BCBlock.banDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"), "{banDuration}");
        BCBlock.banMessage = BCBlock.banMessage.replace(BCBlock.banDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"), "{banDuration}");
        BCBlock.kickMessage = BCBlock.kickMessage.replace(BCBlock.banDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"), "{banDuration}");
        BCBlock.muteMessage = BCBlock.muteMessage.replace(BCBlock.banDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"), "{banDuration}");
        // {muteDuration} replacement
        BCBlock.warnMessage = BCBlock.warnMessage.replace(BCBlock.muteDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"), "{muteDuration}");
        BCBlock.banMessage = BCBlock.banMessage.replace(BCBlock.muteDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"), "{muteDuration}");
        BCBlock.kickMessage = BCBlock.kickMessage.replace(BCBlock.muteDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"), "{muteDuration}");
        BCBlock.muteMessage = BCBlock.muteMessage.replace(BCBlock.muteDuration
                .replace("s", " Second/s")
                .replace("m", " Minute/s")
                .replace("h", " Hour/s"), "{muteDuration}");
    }
}
