package net.mctournaments.bukkit.utils.message;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.DARK_GREEN;
import static org.bukkit.ChatColor.DARK_PURPLE;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.LIGHT_PURPLE;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

public enum MessageType {

    BLANK("%s"),
    ERROR(RED + "\u00BB" + DARK_RED + "\u00BB " + RED + "%s"),
    INFO(LIGHT_PURPLE + "\u00BB" + DARK_PURPLE + "\u00BB " + LIGHT_PURPLE + "%s"),
    INFO_ALTERNATIVE(AQUA + "\u00BB" + DARK_AQUA + "\u00BB " + AQUA + "%s"),
    SUCCESS(GREEN + "\u00BB" + DARK_GREEN + "\u00BB " + GREEN + "%s"),
    WARNING(YELLOW + "\u00BB" + GOLD + "\u00BB " + YELLOW + "%s");

    private final String msg;

    MessageType(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

}