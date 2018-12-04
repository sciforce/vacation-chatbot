package com.vacation_bot.spring.exception;

/**
 * Throw this exception for user retrieving operation.
 */
public class NoUserFoundException extends RuntimeException {

    public NoUserFoundException( String message ) {
        super( message );
    }
}
