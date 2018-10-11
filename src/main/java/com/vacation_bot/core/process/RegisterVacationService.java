package com.vacation_bot.core.process;

import com.vacation_bot.core.BaseService;
import com.vacation_bot.domain.CustomizedSentence;
import com.vacation_bot.domain.models.UserModel;
import com.vacation_bot.domain.models.VacationModel;
import com.vacation_bot.domain.models.VacationTotalModel;
import com.vacation_bot.repositories.RepositoryFactory;
import com.vacation_bot.shared.SharedConstants;
import com.vacation_bot.shared.logging.VacationBotLoggingMessages;
import com.vacation_bot.spring.exception.RepositoryException;
import org.springframework.integration.annotation.ServiceActivator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.UUID;

/**
 * Registers a vacation for the user.
 */
public class RegisterVacationService extends BaseService {

    /**
     * The default total vacation days.
     */
    private static final int DEFAULT_VACATION_TOTAL_DAYS = 20;

    public RegisterVacationService( RepositoryFactory factory ) {
        super( factory );
    }

    @ServiceActivator
    String registerVacation( final CustomizedSentence customizedSentence ) {
        getLogger().info( VacationBotLoggingMessages.REGISTER_VACATION_CHAIN.getMessage() );
        //TODO add verification for a single name and two days. Days order?
        LocalDate startDate = LocalDate.parse( customizedSentence.getDates().get( 0 ) );
        LocalDate endDate = LocalDate.parse( customizedSentence.getDates().get( 1 ) );
        String userName = customizedSentence.getPersons().isEmpty() ? customizedSentence.getUserExternalCode() : customizedSentence.getPersons().get( 0 );
        return createVacation( userName, startDate, endDate );
    }

    private String createVacation( final String userName, final LocalDate startDate, final LocalDate endDate ) {
        int currentYear = Calendar.getInstance().get( Calendar.YEAR );
        int period = (int) ChronoUnit.DAYS.between( startDate, endDate );

        UserModel user = getUserModelRepository().findByNameOrAliases( userName, userName )
                .orElseThrow( () -> new RepositoryException( SharedConstants.USER_NOT_FOUND_MESSAGE ) );

        final VacationTotalModel vacationTotal = getVacationTotalRepository().findByUserIdAndYear( user.getId(), currentYear );
        if ( vacationTotal == null ) {
            VacationTotalModel newVacationTotal = new VacationTotalModel();
            newVacationTotal.setUserId( user.getId() );
            newVacationTotal.setVacationTotal( DEFAULT_VACATION_TOTAL_DAYS );
            newVacationTotal.setYear( currentYear );
            return compareVacationDaysAndReservedDays( newVacationTotal, user, startDate, endDate, period );
        } else {
            return compareVacationDaysAndReservedDays( vacationTotal, user, startDate, endDate, period );
        }
    }

    private String compareVacationDaysAndReservedDays( final VacationTotalModel vacationTotal,
                                                       final UserModel user,
                                                       final LocalDate startDate,
                                                       final LocalDate endDate,
                                                       final int period) {
        String response;
        if ( vacationTotal.getVacationTotal() >= period ) {
            vacationTotal.setVacationTotal( vacationTotal.getVacationTotal() - period );

            getVacationTotalRepository().save( vacationTotal );

            saveVacationRecord( user, startDate, endDate, period );
            response = vacationTotal.getVacationTotal() > 0 ? "The registration of your vacation from " + startDate + " to " + endDate +
                    " was successfully completed! You have left " + vacationTotal.getVacationTotal() + " days"
                    : "The registration of your vacation from " + startDate + " to " + endDate +
                    " was successfully completed! You don't have vacation days";
        } else {
            response = vacationTotal.getVacationTotal() > 0 ? "You can not receive vacation. You have "
                    + vacationTotal.getVacationTotal() + " days." : "You can not receive vacation. You have no vacation";
        }
        return response;
    }

    private void saveVacationRecord( final UserModel user, final LocalDate startDate, final LocalDate endDate, final int period ) {
        final VacationModel vacationModel = new VacationModel();
        vacationModel.setUserId( user.getId() );
        vacationModel.setDialogId( String.valueOf( UUID.randomUUID() ) );
        vacationModel.setDays( period );
        LocalTime localTime = LocalTime.now();
        vacationModel.setStartDate( LocalDateTime.of( startDate, localTime )
                .atZone( ZoneId.systemDefault() ).toInstant().toEpochMilli() );
        vacationModel.setEndDate( LocalDateTime.of( endDate, localTime )
                .atZone( ZoneId.systemDefault() ).toInstant().toEpochMilli() );
        getVacationModelRepository().save( vacationModel );
    }
}
