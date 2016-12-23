package net.mctournaments.bukkit.utils.message;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.mctournaments.bukkit.utils.packetwrappers.WrapperPlayServerChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {

    public static String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String format(MessageType type, String format, Object... args) {
        return colorize(String.format(type.getMsg(), String.format(format, args)));
    }

    public static String format(String format, Object... args) {
        return format(MessageType.BLANK, format, args);
    }

    public static void send(CommandSender sender, MessageType type, String format, Object... args) {
        sender.sendMessage(format(type, format, args));
    }

    public static void send(CommandSender sender, String format, Object... args) {
        send(sender, MessageType.BLANK, format, args);
    }

    public static void sendActionBar(Player player, MessageType type, String format, Object... args) {
        WrapperPlayServerChat packet = new WrapperPlayServerChat();
        packet.setMessage(WrappedChatComponent.fromText(format(type, format, args)));
        packet.setPosition((byte) 2);
        packet.sendPacket(player);
    }

    public static void sendActionBar(Player player, String format, Object... args) {
        sendActionBar(player, MessageType.BLANK, format, args);
    }

    public static void broadcast(MessageType type, String format, Object... args) {
        Bukkit.broadcastMessage(format(type, format, args));
    }

    public static void broadcast(String format, Object... args) {
        broadcast(MessageType.BLANK, format, args);
    }

}
