package com.vacation_bot.core.services;

import me.ramswaroop.jbot.core.slack.models.User;

/**
 * Responsible for managing user data.
 */
public interface UserPort {

    /**
     * Saves user to the system.
     * @param user the user to save.
     */
    void save( final User user );
}
