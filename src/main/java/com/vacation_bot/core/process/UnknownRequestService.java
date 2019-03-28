package com.vacation_bot.core.process;

import com.vacation_bot.core.BaseService;
import com.vacation_bot.domain.CustomizedSentence;
import com.vacation_bot.repositories.RepositoryFactory;
import com.vacation_bot.shared.logging.VacationBotLoggingMessages;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * Process unclassified user request.
 */
public class UnknownRequestService extends BaseService {

    public UnknownRequestService( final RepositoryFactory factory ) {
        super( factory );
    }

    @ServiceActivator
    CustomizedSentence handleRequest( final CustomizedSentence sentence ) {
        getLogger().info( VacationBotLoggingMessages.UNKNOWN_REQUEST.getMessage() );

        sentence.setCurrentResponse( "Remember that I'm only a bot. I just understand things related to vacations. Please repeat :)" );
        return sentence;
    }
}
