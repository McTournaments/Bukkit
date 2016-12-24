package net.mctournaments.bukkit.profile.permissions;

import net.mctournaments.bukkit.profile.Rank;

/**
 * Data access object {@code interface} for {@link Rank} permissions.
 *
 * @author Desetude
 */
public interface RankDataDao {

    default void initialize() {
    }

    /**
     * @param rank to get {@link RankData} of
     * @return {@link RankData} relating to the specified {@link Rank}
     */
    RankData getRankData(Rank rank);

    default void close() {
    }

}
