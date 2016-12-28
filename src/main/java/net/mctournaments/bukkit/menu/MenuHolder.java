package net.mctournaments.bukkit.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * {@link InventoryHolder} for {@link Menu}s so the {@link Menu}
 * of an {@link Inventory} can be accessed with {@link MenuHolder#getMenu().
 */
class MenuHolder implements InventoryHolder {

    private final Menu menu;
    private final Inventory inventory;

    MenuHolder(Menu menu, Inventory inventory) {
        this.menu = menu;
        this.inventory = inventory;
    }

    Menu getMenu() {
        return this.menu;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

}
