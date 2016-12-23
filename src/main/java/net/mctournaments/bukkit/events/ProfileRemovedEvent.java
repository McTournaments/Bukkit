package net.mctournaments.bukkit.events;

import net.mctournaments.bukkit.profile.Profile;
import org.bukkit.event.HandlerList;

public class ProfileRemovedEvent extends ProfileEvent {

    private static final HandlerList handlers = new HandlerList();

    public ProfileRemovedEvent(Profile profile) {
        super(profile);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
