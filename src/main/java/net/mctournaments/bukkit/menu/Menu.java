package net.mctournaments.bukkit.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Allows the easy creation of {@link Inventory}s with {@link ItemStack}s
 * which have actions run when a {@link Player} clicks on them.
 */
public class Menu {

    private final Inventory inventory;
    private final Map<Integer, MenuItem> menuItems = new HashMap<>();
    private final Consumer<Player> closeConsumer;

    private Menu(String title, int size, Consumer<Player> onClose) {
        this.inventory = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(null, size, title)), size, title);
        this.closeConsumer = onClose;
    }

    public static Menu.Builder builder() {
        return new Menu.Builder();
    }

    /**
     * Adds the specified {@link MenuItem} in the {@link Inventory}
     * at the specified index. If this is done when at least one
     * {@link Player} has the {@link Inventory} open,
     * {@link this#update()} should be called to update the view
     * for the {@link Player}.
     *
     * @param item The {@link MenuItem} to add
     * @param index The index to add the {@link MenuItem} at in the {@link Inventory}
     * @return {@code this} for chaining
     */
    public Menu withItem(MenuItem item, int index) {
        this.menuItems.put(index, item);
        return this;
    }

    void consume(MenuItemClickEvent event) {
        if (this.menuItems.containsKey(event.getIndex())) {
            this.menuItems.get(event.getIndex()).getAction().accept(event);
        }
    }

    void close(Player player) {
        this.closeConsumer.accept(player);
    }

    /**
     * Updates the inventory by setting all the {@link ItemStack}s in the inventory
     * to the correct ones from each {@link MenuItem}'s action.
     */
    public void update() {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (this.menuItems.containsKey(i)) {
                this.inventory.setItem(i, this.menuItems.get(i).getIconSupplier().get());
            } else {
                this.inventory.clear(i);
            }
        }
    }

    /**
     * Opens up the {@link Menu}'s {@link Inventory} for all the specified
     * {@link Player}s.
     *
     * @param players The {@link Player}s that should have the {@link Menu} opened
     */
    public void send(Iterable<Player> players) {
        this.update();

        for (Player player : players) {
            player.openInventory(this.inventory);
        }
    }

    /**
     * Opens up the {@link Menu}'s {@link Inventory} for all the specified
     * {@link Player}s.
     *
     * @param players The {@link Player}s that should have the {@link Menu} opened
     */
    public void send(Player... players) {
        this.send(Arrays.asList(players));
    }

    public int getSize() {
        return this.inventory.getSize();
    }

    public String getTitle() {
        return this.inventory.getTitle();
    }

    public Consumer<Player> getCloseConsumer() {
        return this.closeConsumer;
    }

    public static class Builder {

        private String title;
        private int size;
        private Consumer<Player> closeConsumer = (player) -> {
        };

        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder closeConsumer(Consumer<Player> closeConsumer) {
            this.closeConsumer = closeConsumer;
            return this;
        }

        public Menu build() {
            return new Menu(this.title, this.size, this.closeConsumer);
        }

    }

}
