package com.vacation_bot.gateway.outbound.slack;

import com.vacation_bot.shared.logging.AbstractLoggingAware;
import com.vacation_bot.shared.logging.VacationBotLoggingMessages;
import com.vacation_bot.spring.exception.NoUserFoundException;
import me.ramswaroop.jbot.core.slack.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

/**
 * The gateway that manages the outbound iterations with Slack API.
 */
@Component
public class SlackApiGateway extends AbstractLoggingAware implements SlackApiPort {

    @Value( "${slackBotToken}" )
    private String slackToken;

    /**
     * Endpoint for Slack Api
     */
    @Value( "${slackApi}" )
    private String slackApi;

    /**
     * Manages interactions through REST.
     */
    private final RestOperations restTemplate;

    public SlackApiGateway( final RestOperations aRestTemplate ) {
        restTemplate = aRestTemplate;
    }

    @Override
    public User getUser( final String userId ) {
        final String path = getUserConnectApi() + "&user=" + userId;
        final ResponseEntity<UserResponse> response = restTemplate.getForEntity( path, UserResponse.class );
        final User retrievedUser = response.getBody().getUser();
        if ( retrievedUser == null ) {
            getLogger().error( VacationBotLoggingMessages.USER_NOT_FOUND.getMessage(), response.getBody().getError() );
            throw new NoUserFoundException( response.getBody().getError() );
        }
        return retrievedUser;
    }

    private String getUserConnectApi() {
        return slackApi + "/users.info?token=" + slackToken;
    }

    public static class UserResponse {
        private boolean ok;
        private User user;
        private String error;

        public boolean isOk() {
            return ok;
        }

        public User getUser() {
            return user;
        }

        public String getError() {
            return error;
        }
    }
}
