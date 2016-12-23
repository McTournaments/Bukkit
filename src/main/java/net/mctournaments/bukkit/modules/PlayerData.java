package net.mctournaments.bukkit.modules;

import com.google.inject.Inject;
import com.harryfreeborough.modularity.Module;
import net.mctournaments.bukkit.profile.ProfileManager;
import net.mctournaments.bukkit.profile.permissions.PermissionsManager;
import net.mctournaments.bukkit.profile.Profile;
import net.mctournaments.bukkit.profile.storage.ProfileStorageDao;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.UUID;

@Module(name = "PlayerData")
public class PlayerData implements Listener {

    @Inject private Plugin plugin;
    @Inject private ProfileManager profileManager;
    @Inject private PermissionsManager permissionsManager;

    @EventHandler
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent event) {
        ProfileStorageDao dao = this.profileManager.getStorageDao();

        UUID uuid = event.getUniqueId();
        Optional<Profile> optProfile = dao.getProfileData(uuid);

        String username = event.getName();
        String ip = event.getAddress().getHostAddress();

        long join = System.currentTimeMillis();

        Profile profile;
        if (!optProfile.isPresent()) {
            profile = new Profile(uuid);
            profile.setLastKnownUsername(username);
            profile.setLastKnownIp(ip);
            profile.setFirstJoin(join);
            profile.setLastJoin(join);

            dao.insert(profile);
        } else {
            profile = optProfile.get();

            if (!profile.getLastKnownUsername().equals(username)) {
                profile.setLastKnownUsername(username);
                dao.updateUsername(uuid, username);
            }

            if (!profile.getLastKnownIp().equals(ip)) {
                profile.setLastKnownIp(ip);
                dao.updateIp(uuid, ip);
            }

            profile.setLastJoin(join);
            dao.setLastJoin(uuid, join);
        }

        this.profileManager.addProfile(profile);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.permissionsManager.attachAndSet(this.profileManager.getThrowableProfile(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Profile profile = this.profileManager.getThrowableProfile(event.getPlayer());
        this.profileManager.removeProfile(profile);
    }

}
