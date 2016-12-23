package net.mctournaments.bukkit.modules;

import com.harryfreeborough.modularity.Module;
import net.mctournaments.bukkit.utils.logging.Logging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Module(name = "Join")
public class OpBlocker implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            player.setOp(false);
            Logging.info("Removed op from " + player.getName());
        }
    }

}
