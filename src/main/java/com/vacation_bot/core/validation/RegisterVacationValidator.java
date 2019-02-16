package com.vacation_bot.core.validation;

import com.vacation_bot.domain.CustomizedSentence;
import com.vacation_bot.shared.logging.VacationBotLoggingMessages;
import org.springframework.integration.annotation.Filter;

/**
 * Validates that there is all necessary data for executing requested process.
 */
public class RegisterVacationValidator extends BaseValidator {

    public static final int REQUIRED_DATES_COUNT = 2;

    @Filter
    public boolean validateRequest( final CustomizedSentence customizedSentence ) {
        //TODO check that startDate is lower than endDate
        boolean datesAreValid = customizedSentence.getDates().size() == REQUIRED_DATES_COUNT;
        if ( !datesAreValid ) {
            getLogger().warn( VacationBotLoggingMessages.REQUEST_IS_INVALID.getMessage(), customizedSentence.getCustomizedSentence() );
            customizedSentence.setCurrentResponse( "I don't understand dates you provided. " +
                "The example format: from dd.MM.yy to dd.MM.yy" );
        }
        return datesAreValid;
    }
}
