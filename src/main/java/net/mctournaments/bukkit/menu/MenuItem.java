package net.mctournaments.bukkit.menu;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents an item in a {@link Menu} which has an {@link ItemStack}
 * representation and an action for when a {@link Player} clicks it.
 */
public class MenuItem {

    private final Supplier<ItemStack> iconSupplier;
    private final Consumer<MenuItemClickEvent> action;

    private MenuItem(Supplier<ItemStack> iconSupplier, Consumer<MenuItemClickEvent> action) {
        this.iconSupplier = iconSupplier;
        this.action = action;
    }

    public static MenuItem.Builder builder() {
        return new MenuItem.Builder();
    }

    public Supplier<ItemStack> getIconSupplier() {
        return this.iconSupplier;
    }

    public Consumer<MenuItemClickEvent> getAction() {
        return this.action;
    }

    public static class Builder {

        private Supplier<ItemStack> iconSupplier;
        private Consumer<MenuItemClickEvent> action = (event) -> {
        };

        public Builder icon(Supplier<ItemStack> iconSupplier) {
            this.iconSupplier = iconSupplier;
            return this;
        }

        public Builder action(Consumer<MenuItemClickEvent> action) {
            this.action = action;
            return this;
        }

        public MenuItem build() {
            checkNotNull(this.iconSupplier, "The icon supplier must be set in order to build a MenuItem.");
            return new MenuItem(this.iconSupplier, this.action);
        }

    }

}
