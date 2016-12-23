package net.mctournaments.bukkit.events;

import net.mctournaments.bukkit.profile.Profile;
import org.bukkit.event.Event;

public abstract class ProfileEvent extends Event {

    protected final Profile profile;

    public ProfileEvent(Profile profile) {
        this(profile, false);
    }

    public ProfileEvent(Profile profile, boolean async) {
        super(async);
        this.profile = profile;
    }

    public Profile getProfile() {
        return this.profile;
    }

}
