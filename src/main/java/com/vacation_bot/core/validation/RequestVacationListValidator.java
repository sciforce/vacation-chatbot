package com.vacation_bot.core.validation;

import com.vacation_bot.domain.CustomizedSentence;
import com.vacation_bot.shared.logging.VacationBotLoggingMessages;
import org.springframework.integration.annotation.Filter;

/**
 * Validates that there is all necessary data for executing requested process.
 */
public class RequestVacationListValidator extends BaseValidator {

    public static final int REQUIRED_DATES_COUNT = 2;

    public static final int REQUIRED_NUMBERS_COUNT = 1;

    @Filter
    public boolean validateRequest( final CustomizedSentence customizedSentence ) {
        //TODO check that startDate is lower than endDate
        boolean datesAreValid = customizedSentence.getDates().size() == REQUIRED_DATES_COUNT;
        boolean numberIsValid = customizedSentence.getNumbers().size() == REQUIRED_NUMBERS_COUNT;
        if ( !datesAreValid && !numberIsValid ) {
            getLogger().warn( VacationBotLoggingMessages.REQUEST_IS_INVALID.getMessage(), customizedSentence.getCustomizedSentence() );
            customizedSentence.setCurrentResponse( "Please provide the requested period one more time or amount of vacations. " +
                "The example format: from dd.MM.yy to dd.MM.yy" ); }
        return datesAreValid || numberIsValid;
    }
}
