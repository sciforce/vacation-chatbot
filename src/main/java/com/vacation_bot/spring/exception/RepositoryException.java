package com.vacation_bot.spring.exception;

/**
 * Throw this exception if repository return null.
 */
public class RepositoryException extends RuntimeException {

    public RepositoryException(String message) {
        super(message);
    }
}
