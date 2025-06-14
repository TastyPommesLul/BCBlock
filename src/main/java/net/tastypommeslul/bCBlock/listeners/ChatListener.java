package net.tastypommeslul.bCBlock.listeners;

import io.papermc.paper.event.player.ChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.tastypommeslul.bCBlock.BCBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.Duration;

public class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent e) {
        Component message = e.message();
        TextComponent textMessage = (TextComponent) message;
        Player p = e.getPlayer();

        // Split the message into words
        String[] messageWords = textMessage.content().split(" ");



        // Check each word against the blocked words list
        for (String messageWord : messageWords) {
            // Remove all non-alphabetic characters and convert to lowercase
            String filteredWord = messageWord.replaceAll("[^a-zA-Z]", "").toLowerCase();

            // Check if the filtered word matches any blocked word
            for (String blockedWord : BCBlock.blockedWords) {
                if (filteredWord.equals(blockedWord.toLowerCase())) {
                    e.setCancelled(true);
                    BCBlock.warnMessage = BCBlock.warnMessage.replace("{word}", messageWord);
                    BCBlock.banMessage = BCBlock.banMessage.replace("{word}", messageWord);
                    BCBlock.kickMessage = BCBlock.kickMessage.replace("{word}", messageWord);
                    BCBlock.warnMessage = BCBlock.warnMessage.replace("{player}", p.getName());
                    BCBlock.banMessage = BCBlock.banMessage.replace("{player}", p.getName());
                    BCBlock.kickMessage = BCBlock.kickMessage.replace("{player}", p.getName());
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
                        default:
                            p.sendMessage(Component.text(BCBlock.warnMessage));
                            break;
                    }

                    return;
                }
            }
        }
    }
}
