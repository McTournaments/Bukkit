package net.mctournaments.bukkit.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof MenuHolder) || !(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Menu menu = ((MenuHolder) holder).getMenu();
        MenuItemClickEvent menuEvent = new MenuItemClickEvent(menu, (Player) event.getWhoClicked(), event.getClick(), event.getSlot());

        menu.consume(menuEvent);

        event.setCancelled(menuEvent.isClickCancelled());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof MenuHolder) || !(event.getPlayer() instanceof Player)) {
            return;
        }

        ((MenuHolder) holder).getMenu().close((Player) event.getPlayer());
    }

}
