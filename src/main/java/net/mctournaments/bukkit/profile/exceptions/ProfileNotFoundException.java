package net.mctournaments.bukkit.profile.exceptions;

import net.mctournaments.common.profile.Profile;

/**
 * {@link RuntimeException} for when a {@link Profile} is not found.
 *
 * @author Desetude
 */
public class ProfileNotFoundException extends RuntimeException {

    /**
     * @param message describing {@code this} {@link Exception}
     */
    public ProfileNotFoundException(String message) {
        super(message);
    }

}
