package com.vacation_bot.shared.logging;

/**
 * Logging messages specific for current application.
 */
public enum VacationBotLoggingMessages {

    STAGE_LOGGING( "The {} stage!" ),
    CLASSIFICATION_RESULT( "The classification result is {}" ),
    REGISTER_VACATION_CHAIN( "The chain of processing request vacation sentence class" ),
    DAYS_LEFT_CHAIN( "The chain of processing days left request" );

    private String message;

    VacationBotLoggingMessages( final String description ) {
        message = description;
    }

    public String getMessage() {
        return message;
    }
}
