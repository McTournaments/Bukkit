package net.mctournaments.bukkit.utils.items.gadget;

import org.bukkit.entity.Player;

public interface Gadget {

    String getName();

    /**
     *
     *
     * @param player that has interacted with {@code this} {@link Gadget}
     */
    void run(Player player);

    /**
     * Returns cooldown of {@code this} {@link Gadget} in ticks.
     * If the player attempts to interact with {@code this} {@link Gadget}
     * when it is under the cooldown, they will be sent a message stating the
     * Gadget is under the cooldown and cannot be used along with the time
     * until they can next use it.
     *
     * @return cooldown of {@code this} {@link Gadget}
     */
    default int getCooldown() {
        return 0;
    }

}
