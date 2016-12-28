package net.mctournaments.bukkit.utils.items.gadget;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface Gadget {

    public static Gadget create(String name, Consumer<Player> action, int cooldown) {
        return new Gadget() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run(Player player) {
                action.accept(player);
            }

            @Override
            public int getCooldown() {
                return cooldown;
            }
        };
    }

    public static Gadget create(String name, Consumer<Player> action) {
        return create(name, action, 0);
    }

    String getName();

    /**
     * Runs the action relating to {@code this} {@link Gadget}.
     *
     * @param player that has interacted with {@code this} {@link Gadget}
     */
    void run(Player player);

    /**
     * Returns cooldown of {@code this} {@link Gadget} in milliseconds.
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
