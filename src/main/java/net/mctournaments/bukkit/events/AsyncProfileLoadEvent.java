package net.mctournaments.bukkit.events;

import net.mctournaments.common.profile.Profile;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncProfileLoadEvent extends ProfileEvent {

    private static final HandlerList handlers = new HandlerList();

    private final AsyncPlayerPreLoginEvent preLoginEvent;

    public AsyncProfileLoadEvent(Profile profile, AsyncPlayerPreLoginEvent preLoginEvent) {
        super(profile, true);
        this.preLoginEvent = preLoginEvent;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public AsyncPlayerPreLoginEvent getPreLoginEvent() {
        return this.preLoginEvent;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
