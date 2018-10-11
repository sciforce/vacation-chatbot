package com.vacation_bot.shared.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience base class for objects that wish to have a logger injected into them.
 */
public class AbstractLoggingAware {

    /**
     * The logger to use.
     */
    private Logger logger;

    protected AbstractLoggingAware() {
        logger = LoggerFactory.getLogger( "Nullable logger" );
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger( final Logger aLogger ) {
        logger = aLogger;
    }
}
