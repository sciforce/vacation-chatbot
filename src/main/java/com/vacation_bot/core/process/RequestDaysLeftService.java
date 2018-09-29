package com.vacation_bot.core.process;

import com.vacation_bot.core.BaseService;
import com.vacation_bot.core.customization.CustomizedSentence;
import com.vacation_bot.domain.models.UserModel;
import com.vacation_bot.domain.models.VacationTotalModel;
import com.vacation_bot.repositories.RepositoryFactory;
import com.vacation_bot.shared.SharedConstants;
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
    String calcuateDaysLeft( final CustomizedSentence customizedSentence ) {
        //TODO Add validation of person names amount
        String userName = customizedSentence.getPersons().get( 0 );
        int currentYear = Calendar.getInstance().get( Calendar.YEAR );
        UserModel user = getUserModelRepository().findByNameOrAliases( userName, userName )
                .orElseThrow( () -> new RepositoryException( SharedConstants.USER_NOT_FOUND_MESSAGE ) );
        VacationTotalModel vacationTotal = getVacationTotalRepository().findByUserIdAndYear( user.getId(), currentYear );
        return "You still have " + vacationTotal.getVacationTotal() + " days left.";
    }
}
