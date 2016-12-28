package net.mctournaments.bukkit.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuItemClickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Menu menu;
    private final Player player;
    private final ClickType clickType;
    private final int index;
    private boolean clickCancelled = true;

    public MenuItemClickEvent(Menu menu, Player player, ClickType clickType, int index) {
        this.menu = menu;
        this.player = player;
        this.clickType = clickType;
        this.index = index;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Returns the {@link Menu} that was clicked by the {@link Player}.
     *
     * @return The {@link Menu} clicked
     */
    public Menu getMenu() {
        return this.menu;
    }

    /**
     * Returns the {@link Player} who clicked the {@link Menu}.
     *
     * @return {@link Player} who clicked the {@link Menu}
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the type of click the {@link Player} did onto the {@link Menu}
     *
     * @return Type of click by the {@link Player}
     */
    public ClickType getClickType() {
        return this.clickType;
    }

    /**
     * Returns the index of the {@link ItemStack} the {@link Player} clicked
     * in the {@link Menu}.
     *
     * @return index of {@link ItemStack} clicked
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns whether the {@link InventoryClickEvent} relevant to {@code this}
     * {@link MenuItemClickEvent} should be cancelled.
     * Default is true.
     *
     * @return whether the item click should be cancelled
     */
    public boolean isClickCancelled() {
        return this.clickCancelled;
    }

    /**
     * Sets whether the {@link InventoryClickEvent} relevant to {@code this}
     * {@link MenuItemClickEvent} should be cancelled.
     * Default is true.
     *
     * @param clickCancelled Whether the item click should be cancelled
     */
    public void setClickCancelled(boolean clickCancelled) {
        this.clickCancelled = clickCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
