package net.mctournaments.bukkit.utils.items.gadget;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;

import com.harryfreeborough.modularity.injector.AutoRegister;
import net.mctournaments.bukkit.utils.message.MessageType;
import net.mctournaments.bukkit.utils.message.Messages;
import net.mctournaments.bukkit.utils.nms.NbtTags;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@AutoRegister
public class GadgetFactory implements Listener {

    public static final String GADGET_NBT_KEY = "gadget";

    private final Map<String, Gadget> gadgets;
    private final Map<CooldownKey, Long> lastUses;

    public GadgetFactory() {
        this.gadgets = new HashMap<>();
        this.lastUses = new HashMap<>();
    }

    public void registerGadget(Gadget gadget) {
        checkArgument(gadget != null && !this.gadgets.keySet().contains(gadget.getName()),
                "A gadget with the specified gadget's name '" + gadget.getName() + "' is already registered.");
        this.gadgets.put(gadget.getName(), gadget);
    }

    public void unregisterGadget(Gadget gadget) {
        this.gadgets.remove(checkNotNull(gadget).getName());
    }

    public Optional<Gadget> getGadget(String name) {
        checkArgument(name != null && !name.isEmpty(), "name cannot be null or empty.");
        return Optional.ofNullable(this.gadgets.get(name));
    }

    public long getCooldownTimeLeft(Player player, Gadget gadget) {
        checkNotNull(player, "player");
        checkNotNull(gadget, "gadget");

        CooldownKey key = new CooldownKey(gadget, player.getUniqueId());
        if (!this.lastUses.containsKey(key)) {
            return 0;
        }

        long timeLeft = gadget.getCooldown() - (System.currentTimeMillis() - this.lastUses.get(key));
        if (timeLeft <= 0)

        {
            this.lastUses.remove(key);
            timeLeft = 0;
        }

        return timeLeft;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.lastUses.entrySet().removeIf(entry -> entry.getKey().uuid.equals(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if ((event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) || event.getItem() == null) {
            return;
        }

        NbtTags tags = NbtTags.create(event.getItem());
        if (!tags.hasKey(GADGET_NBT_KEY)) {
            return;
        }

        String gadgetName = tags.getString(GADGET_NBT_KEY);
        Gadget gadget = this.getGadget(gadgetName).orElseThrow(() -> new IllegalArgumentException("Failed to get gadget with name: " + gadgetName));

        event.setCancelled(true);

        Player player = event.getPlayer();
        long timeLeft = this.getCooldownTimeLeft(player, gadget);
        if (timeLeft > 0) {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft);
            if (seconds == 0) {//Make sure it doesn't display as 0 seconds left.
                seconds = 1;
            }

            long minutes = TimeUnit.SECONDS.toMinutes(seconds);
            seconds -= TimeUnit.MINUTES.toSeconds(minutes);
            Messages.send(player, MessageType.ERROR, "You must wait another " + YELLOW +
                    (minutes >= 1 ? minutes + " minutes and " : "") + seconds + " seconds " + RED + "before using this!");
            return;
        }

        gadget.run(player);
        this.lastUses.put(new CooldownKey(gadget, player.getUniqueId()), System.currentTimeMillis());
    }

    private class CooldownKey {

        private final Gadget gadget;
        private final UUID uuid;

        public CooldownKey(Gadget gadget, UUID uuid) {
            this.gadget = checkNotNull(gadget, "gadget");
            this.uuid = checkNotNull(uuid, "uuid");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            CooldownKey gadgetKey = (CooldownKey) o;

            if (!this.gadget.equals(gadgetKey.gadget)) {
                return false;
            }
            return this.uuid.equals(gadgetKey.uuid);
        }

        @Override
        public int hashCode() {
            int result = this.gadget.hashCode();
            result = 31 * result + this.uuid.hashCode();
            return result;
        }

    }

}
