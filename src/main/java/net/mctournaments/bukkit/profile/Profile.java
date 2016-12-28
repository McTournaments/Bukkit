package net.mctournaments.bukkit.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Holds data of a user.
 *
 * @author Desetude
 */
public class Profile {

    private final UUID uuid;

    private NavigableRankSet ranks;

    private String lastKnownUsername;
    private String lastKnownIp;

    private List<String> knownUsernames;
    private List<String> knownIps;

    private Map<String, Boolean> settings;

    private long firstJoin;
    private long lastJoin;

    public Profile(UUID uuid) {
        this.uuid = checkNotNull(uuid, "uuid");

        this.ranks = new NavigableRankSet();
        this.ranks.add(Rank.DEFAULT);

        this.knownUsernames = new ArrayList<>();
        this.knownIps = new ArrayList<>();
        this.settings = new HashMap<>();
    }

    /**
     * @return the global unique identifier of the user
     */
    public UUID getUniqueId() {
        return this.uuid;
    }

    /**
     * @return the last known username of the user
     */
    public String getLastKnownUsername() {
        return this.lastKnownUsername;
    }

    /**
     * @param username to set the last known username of the user to
     */
    public void setLastKnownUsername(String username) {
        this.lastKnownUsername = checkNotNull(username, "username");
        this.addKnownUsername(this.lastKnownUsername);
    }

    /**
     * @return the last known ip address of the user
     */
    public String getLastKnownIp() {
        return this.lastKnownIp;
    }

    /**
     * @param ip to set the last known ip address of the user to
     */
    public void setLastKnownIp(String ip) {
        this.lastKnownIp = checkNotNull(ip, "ip");
        this.addKnownIp(this.lastKnownIp);
    }

    /**
     * @return {@link List} all known usernames of the user
     */
    public List<String> getKnownUsernames() {
        return Collections.unmodifiableList(this.knownUsernames);
    }

    /**
     * @param usernames to set the collection of all known usernames to
     */
    public void setKnownUsernames(List<String> usernames) {
        this.knownUsernames = checkNotNull(usernames, "usernames");
    }

    /**
     * @param username to add to collection of all known usernames of the user
     */
    public void addKnownUsername(String username) {
        this.knownUsernames.add(checkNotNull(username, "username"));
    }

    /**
     * @return {@link List} of all know ip addresses of the user
     */
    public List<String> getKnownIps() {
        return Collections.unmodifiableList(this.knownIps);
    }

    /**
     * @param ips to set the collection of all known ips to
     */
    public void setKnownIps(List<String> ips) {
        this.knownIps = checkNotNull(ips, "ips");
    }

    /**
     * @param ip to add to collection of all known ip addresses of the user
     */
    public void addKnownIp(String ip) {
        this.knownIps.add(checkNotNull(ip));
    }

    /**
     * @return {@link Map} of all the user's settings in format <Setting Identifier, Value>
     */
    public Map<String, Boolean> getSettings() {
        return Collections.unmodifiableMap(this.settings);
    }

    /**
     * Sets all the user's settings to the specified {@link Map}<Setting Identifier, Value>.
     *
     * @param settings to be set
     */
    public void setSettings(Map<String, Boolean> settings) {
        this.settings = checkNotNull(settings);
    }

    /**
     * Sets the setting identified by the specified identifier to the specified value.
     *
     * @param identifier of the target setting
     * @param value      you want to change the target setting to
     */
    public void setSetting(String identifier, boolean value) {
        this.settings.put(checkNotNull(identifier, "identifier"), value);
    }

    /**
     * @return the first time the user joined the network by milliseconds since standard epoch of 1/1/1970
     */
    public long getFirstJoin() {
        return this.firstJoin;
    }

    /**
     * @param ms since standard epoch of 1/1/1970 to set the user's first join of the network to
     */
    public void setFirstJoin(long ms) {
        this.firstJoin = ms;
    }

    /**
     * @return the last time the user joined by milliseconds since standard epoch of 1/1/1970
     */
    public long getLastJoin() {
        return this.lastJoin;
    }

    /**
     * @param ms since standard epoch of 1/1/1970 to set the user's last join of the network to
     */
    public void setLastJoin(long ms) {
        this.lastJoin = ms;
    }

    /**
     * Gets all the ranks the user has.
     * A user cannot have two ranks that are on the same ladder as each other, e.g. Donator and Donator+
     * but they can have ranks on two different ladders e.g. Mod and Donator.
     *
     * @return {@link NavigableRankSet} of all ranks the user has
     */
    public NavigableRankSet getRanks() {
        return this.ranks;
    }

    /**
     * @return rank to be visible in chat and other things such as tab color
     */
    public Rank getVisibleRank() {
        return this.getRanks().asNavigableSet().last();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Profile profile = (Profile) o;

        return this.uuid.equals(profile.getUniqueId());
    }

    public int hashCode() {
        return this.uuid.hashCode();
    }

}
