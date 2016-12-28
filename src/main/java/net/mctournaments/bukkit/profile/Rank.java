package net.mctournaments.bukkit.profile;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.WHITE;

import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Holds different ranks a {@link Profile} can have.
 *
 * @author Desetude
 */
public enum Rank {

    DEFAULT(null, GRAY + "Default") {
        @Override
        public String getChatFormat() {
            return GRAY + "%s: %s";
        }
    }, DONATOR(DEFAULT, GREEN + "Donor"),
    MOD(DEFAULT, AQUA + "Mod"),
    ADMIN(MOD, RED + "Admin"),
    SUPER_ADMIN(ADMIN, RED + "Admin");

    private final Rank inheritsFrom;
    private final String formattedName;

    Rank(Rank inheritsFrom, String formattedName) {
        this.inheritsFrom = inheritsFrom;
        this.formattedName = formattedName;
    }

    /**
     * @return all ranks in {@code this} ranks inheritance ladder/tree including {@code this}
     */
    public NavigableSet<Rank> inheritanceTree() {
        NavigableSet<Rank> ranks = new TreeSet<>();
        ranks.add(this);

        Rank rank = this.inheritsFrom;

        while (rank != null) {
            ranks.add(rank);
            rank = rank.inheritsFrom;
        }

        return ranks;
    }

    public String getFormattedName() {
        return this.formattedName;
    }

    public String getChatFormat() {
        return this.formattedName + " | %s" + WHITE + ": %s";
    }

}
