package com.vacation_bot.core;

import com.vacation_bot.domain.CustomizedSentence;
import org.springframework.messaging.Message;

/**
 * Service that enters a request sentence into the system.
 * This interface acts as a gateway between our MVC controller and Spring Integration.
 */
public interface InternalTranslationPort {

    /**
     * Process the input sentence and classify it.
     * @param sentence The SI message, containing a json payload representing the sentence to process.
     */
    CustomizedSentence processSentence( Message<CustomizedSentence> sentence );
}
