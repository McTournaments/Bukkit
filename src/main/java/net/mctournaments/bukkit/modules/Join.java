package net.mctournaments.bukkit.modules;

import com.google.inject.Inject;
import com.harryfreeborough.modularity.Module;
import net.mctournaments.bukkit.utils.message.Messages;
import net.mctournaments.bukkit.profile.ProfileManager;
import net.mctournaments.common.profile.Profile;
import net.mctournaments.common.profile.Rank;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Module(name = "Join")
public class Join implements Listener {

    @Inject private ProfileManager profileManager;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = this.profileManager.getThrowableProfile(event.getPlayer());

        String prefix = Messages.colorize(profile.getVisibleRank().getFormattedName()) + " ";
        if (profile.getVisibleRank() == Rank.DEFAULT) {
            prefix = ChatColor.GRAY.toString();
        }

        event.setJoinMessage(prefix + event.getPlayer().getDisplayName() + ChatColor.YELLOW + " has joined!");
    }

}
