package com.vacation_bot.gateway.outbound.slack;

import com.vacation_bot.spring.exception.NoUserFoundException;
import me.ramswaroop.jbot.core.slack.models.User;

public interface SlackApiPort {

    /**
     * Retrieves the user from the Slack API.
     * Hope it'll be fixed in the next releases of jbot library and will be supported out of box.
     * @param userId the unique identifier of the user that should be retrieved from slack.
     * @return the found user.
     * @throws {@link NoUserFoundException} if no user are retrieved.
     */
    User getUser( String userId );
}
