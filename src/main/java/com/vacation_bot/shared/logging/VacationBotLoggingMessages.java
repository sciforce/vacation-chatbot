package com.vacation_bot.shared.logging;

/**
 * Logging messages specific for current application.
 */
public enum VacationBotLoggingMessages {

    STAGE_LOGGING( "The {} stage!" ),
    CLASSIFICATION_RESULT( "The classification result is {}" ),
    REGISTER_VACATION_CHAIN( "The chain of processing request vacation sentence class" ),
    DAYS_LEFT_CHAIN( "The chain of processing days left request" ),
    REQUEST_VACATION_LIST( "The chain of processing vacation list request" ),
    UNKNOWN_REQUEST( "The chain of processing unknown request." ),
    USER_NOT_FOUND( "The user was not found. Message details: {}." ),
    REQUEST_IS_INVALID( "The request validation failed. See if sentence was properly customized: {}." );

    private String message;

    VacationBotLoggingMessages( final String description ) {
        message = description;
    }

    public String getMessage() {
        return message;
    }
}
