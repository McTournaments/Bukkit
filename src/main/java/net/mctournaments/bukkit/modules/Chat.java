package net.mctournaments.bukkit.modules;

import com.google.inject.Inject;
import com.harryfreeborough.modularity.Module;
import net.mctournaments.bukkit.data.Messages;
import net.mctournaments.bukkit.profile.ProfileManager;
import net.mctournaments.common.profile.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Module(name = "Chat")
public class Chat implements Listener {

    @Inject private ProfileManager profileManager;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Profile profile = this.profileManager.getThrowableProfile(event.getPlayer());

        event.setFormat(Messages.colorize(profile.getVisibleRank().getChatFormat()));
    }

}
