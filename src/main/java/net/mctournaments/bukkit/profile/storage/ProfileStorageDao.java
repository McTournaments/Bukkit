package net.mctournaments.bukkit.profile.storage;

import net.mctournaments.bukkit.profile.Profile;
import net.mctournaments.bukkit.profile.Rank;

import java.util.Optional;
import java.util.UUID;

public interface ProfileStorageDao {

    default void initialize() {
    }

    Optional<Profile> getProfileData(UUID uuid);

    Optional<Profile> getProfileData(String username);

    void insert(Profile profile);

    boolean addRank(UUID uuid, Rank rank);

    boolean removeRank(UUID uuid, Rank rank);

    boolean setRank(UUID uuid, Rank rank);

    boolean addRank(String username, Rank rank);

    boolean removeRank(String username, Rank rank);

    boolean setRank(String username, Rank rank);

    void setLastJoin(UUID uuid, long lastJoin);

    /**
     * Adds the specified username as the last known username and also adds it
     * to the list of all known usernames.
     *
     * @param uuid Player's UUID
     * @param username Username to update player's last known username to
     */
    void updateUsername(UUID uuid, String username);

    /**
     * Adds the specified IP as the last known IP and also adds it
     * to the list of all known IPs.
     *
     * @param uuid Player's UUID
     * @param ip IP to update player's last known IP to
     */
    void updateIp(UUID uuid, String ip);

    void setSetting(UUID uuid, String setting, boolean value);

    default void close() {
    }

}
