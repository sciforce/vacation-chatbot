package com.vacation_bot.core.process;

import com.vacation_bot.core.BaseService;
import com.vacation_bot.domain.CustomizedSentence;
import com.vacation_bot.domain.models.UserModel;
import com.vacation_bot.domain.models.VacationTotalModel;
import com.vacation_bot.repositories.RepositoryFactory;
import com.vacation_bot.shared.SharedConstants;
import com.vacation_bot.shared.logging.VacationBotLoggingMessages;
import com.vacation_bot.spring.exception.RepositoryException;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.Calendar;

/**
 * Process the user request about vacation days left.
 */
public class RequestDaysLeftService extends BaseService {

    public RequestDaysLeftService( final RepositoryFactory factory ) {
        super( factory );
    }

    @ServiceActivator
    CustomizedSentence calculateDaysLeft( final CustomizedSentence customizedSentence ) {
        getLogger().info( VacationBotLoggingMessages.DAYS_LEFT_CHAIN.getMessage() );
        String userName = customizedSentence.getPersons().isEmpty() ? customizedSentence.getUserExternalCode() : customizedSentence.getPersons().get( 0 );
        int currentYear = Calendar.getInstance().get( Calendar.YEAR );
        UserModel user = getUserModelRepository().findById( userName )
                .orElseThrow( () -> new RepositoryException( SharedConstants.USER_NOT_FOUND_MESSAGE ) );
        VacationTotalModel vacationTotal = getVacationTotalRepository().findByUserIdAndYear( user.getId(), currentYear );
        String validDays = vacationTotal != null ? vacationTotal.getVacationTotal() + "" : "no";
        customizedSentence.setCurrentResponse( "You still have " + validDays + " days left." );
        return customizedSentence;
    }
}
