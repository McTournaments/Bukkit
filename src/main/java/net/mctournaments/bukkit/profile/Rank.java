package net.mctournaments.bukkit.profile;

import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Holds different ranks a {@link Profile} can have.
 *
 * @author Desetude
 */
public enum Rank {

    DEFAULT(null, "&7Default") {
        @Override
        public String getChatFormat() {
            return "&7%s: %s";
        }
    }, DONATOR(DEFAULT, "&aDonor"),
    MOD(DEFAULT, "&bMod"),
    ADMIN(MOD, "&cAdmin"),
    SUPER_ADMIN(ADMIN, "&cAdmin");

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
        return this.formattedName + " | %s&f: %s";
    }

}
