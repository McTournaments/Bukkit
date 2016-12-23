package net.mctournaments.bukkit.profile;

import com.google.common.collect.Iterables;
import net.mctournaments.bukkit.profile.exceptions.ProfileNotFoundException;
import net.mctournaments.common.profile.Profile;
import net.mctournaments.common.profile.storage.ProfileStorageDao;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * Manages getting, adding and removing of {@link Profile}.
 *
 * @author Desetude
 */
public class ProfileManager {

    private final Plugin plugin;
    private final ProfileStorageDao storageDao;

    private final Set<Profile> profiles;
    private final ReadWriteLock lock;

    public ProfileManager(Plugin plugin, ProfileStorageDao storageDao) {
        this.plugin = plugin;
        this.storageDao = storageDao;

        this.profiles = new HashSet<>();
        this.lock = new ReentrantReadWriteLock();
    }

    /**
     * Returns {@link Optional#of(Object)} of {@link Profile} if it exists in the local cache, otherwise returns {@link Optional#empty()}.
     *
     * @param player to get data of
     * @return {@link Optional} {@link Profile} of the user
     */
    public Optional<Profile> getProfile(Player player) {
        this.lock.readLock().lock();
        try {
            return this.getProfile(player.getUniqueId());
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Returns {@link Optional#of(Object)} of {@link Profile} if it exists in local cache, otherwise returns {@link Optional#empty()}.
     *
     * @param uuid representing the {@link Profile}
     * @return {@link Optional} {@link Profile} of the user
     */
    public Optional<Profile> getProfile(UUID uuid) {
        this.lock.readLock().lock();
        try {
            return this.profiles.stream().filter(profile -> profile.getUniqueId().equals(uuid)).findAny();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Returns {@link Optional#of(Object)} of {@link Profile} if it exists in local cache, otherwise returns {@link Optional#empty()}.
     *
     * @param username representing the {@link Profile}
     * @return {@link Optional} {@link Profile}
     */
    public Optional<Profile> getProfile(String username) {
        this.lock.readLock().lock();
        try {
            return this.profiles.stream().filter(profile -> profile.getLastKnownUsername().equalsIgnoreCase(username)).findAny();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * Returns the obtained value from {@link this#getProfile(Player)}
     *
     * @param player to get data of
     * @return {@link Profile} of the user
     * @throws ProfileNotFoundException if {@code !{@link Optional#isPresent()}}
     */
    public Profile getThrowableProfile(Player player) {
        return this.getProfile(player).orElseThrow(() -> new ProfileNotFoundException(player.toString()));
    }

    /**
     * Returns the obtained value from {@link this#getProfile(UUID)}
     *
     * @param uuid representing the {@link Profile}
     * @return {@link Profile} of the user
     * @throws ProfileNotFoundException if {@code !{@link Optional#isPresent()}}
     */
    public Profile getThrowableProfile(UUID uuid) {
        return this.getProfile(uuid).orElseThrow(() -> new ProfileNotFoundException(uuid.toString()));
    }

    /**
     * Returns the obtained value from {@link this#getProfile(String)}
     *
     * @param username representing the {@link Profile}
     * @return {@link Profile} of the user
     * @throws ProfileNotFoundException if {@code !{@link Optional#isPresent()}}
     */
    public Profile getThrowableProfile(String username) {
        return this.getProfile(username).orElseThrow(() -> new ProfileNotFoundException(username));
    }

    /**
     * Adds the specified {@link Profile} to the local cache.
     *
     * @param profile to add
     */
    public void addProfile(Profile profile) {
        this.lock.writeLock().lock();
        try {
            this.profiles.add(profile);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Removes the specified {@link Profile} from the local cache.
     *
     * @param profile to remove
     */
    public void removeProfile(Profile profile) {
        this.lock.writeLock().lock();
        try {
            this.profiles.remove(profile);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * @return unmodifiable {@link Collection} of all users in the local cache
     */
    public Iterable<Profile> getProfiles() {
        return Iterables.unmodifiableIterable(this.profiles);
    }

    /**
     * Gets {@link Profile} that does not need to be on the server,
     * will modify on the database if not.
     * <p>
     * Is ran in async if found from database.
     *
     * @param username of the {@link Profile}
     * @param action for querying and modifying the {@link Profile}
     * @param emptyAction the action to do if the {@link Profile} is not found
     */
    public void getProfile(String username, Consumer<Profile> action, Runnable emptyAction) {
        Optional<Profile> serverProfile = this.getProfile(username);
        if (serverProfile.isPresent()) {
            action.accept(serverProfile.get());
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            Optional<Profile> dbProfile = this.storageDao.getProfileData(username);
            Bukkit.getScheduler().runTask(this.plugin, () -> {
                if (dbProfile.isPresent()) {
                    action.accept(dbProfile.get());
                } else {
                    emptyAction.run();
                }
            });
        });
    }

    public ProfileStorageDao getStorageDao() {
        return this.storageDao;
    }

}
