package net.mctournaments.bukkit.events.lifecycle;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InitializationEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
