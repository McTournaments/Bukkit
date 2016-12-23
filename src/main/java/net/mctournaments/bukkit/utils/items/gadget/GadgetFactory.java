package net.mctournaments.bukkit.utils.items.gadget;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.harryfreeborough.modularity.injector.AutoRegister;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AutoRegister
public class GadgetFactory implements Listener {

    private final Map<String, Gadget> gadgets;
    private final Map<CooldownKey, Long> cooldowns;

    public GadgetFactory() {
        this.gadgets = new HashMap<>();
        this.cooldowns = new HashMap<>();
    }

    public void registerGadget(Gadget gadget) {
        checkArgument(!this.gadgets.keySet().contains(gadget.getName()), "A gadget with the specified gadget's name is already registered.");
        this.gadgets.put(gadget.getName(), gadget);
    }

    public void unregisterGadget(Gadget gadget) {
        this.gadgets.remove(checkNotNull(gadget).getName());
    }

    public Gadget getGadget(String name) {
        checkArgument(name != null && !name.isEmpty(), "name cannot be null or empty.");
        return this.gadgets.get(name);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.cooldowns.entrySet().removeIf(entry -> entry.getKey().uuid.equals(event.getPlayer().getUniqueId()));
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
