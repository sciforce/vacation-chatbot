package com.vacation_bot.core.vacation;

import com.vacation_bot.core.BaseService;
import com.vacation_bot.domain.models.UserModel;
import com.vacation_bot.domain.models.VacationModel;
import com.vacation_bot.domain.models.VacationTotalModel;
import com.vacation_bot.repositories.RepositoryFactory;
import com.vacation_bot.spring.exception.RepositoryException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.UUID;

/**
 * Reserve a vacation to the user.
 */
@Service
public class VacationService extends BaseService{

    /**
     * The default total vacation days.
     */
    private static final int DEFAULT_VACATION_TOTAL_DAYS = 20;

    /**
     * The message in case user was not found.
     */
    private static final String USER_NOT_FOUND_MESSAGE = "USER NOT FOUND";

    /**
     * Current year.
     */
    private int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public VacationService(RepositoryFactory factory) {
        super(factory);
    }

    public String createVacation(final String userName, final String _startDate, final String _endDate) {
        LocalDate startDate = LocalDate.parse(_startDate);
        LocalDate endDate = LocalDate.parse(_endDate);

        int period = (int) ChronoUnit.DAYS.between(startDate, endDate);

        UserModel user = getUserModelRepository().findByNameOrAliases(userName, userName)
                .orElseThrow(() -> new RepositoryException(USER_NOT_FOUND_MESSAGE));

        VacationTotalModel vacationTotal = getVacationTotalRepository().findByUserIdAndYear(user.getId(), currentYear);
        if (vacationTotal == null) {
            VacationTotalModel newVacationTotal = new VacationTotalModel();
            newVacationTotal.setUserId(user.getId());
            newVacationTotal.setVacationTotal(DEFAULT_VACATION_TOTAL_DAYS);
            newVacationTotal.setYear(currentYear);
            getVacationTotalRepository().save(newVacationTotal);
            return compareVacationDaysAndReservedDays(newVacationTotal, user, startDate, endDate, period);
        } else {
            return compareVacationDaysAndReservedDays(vacationTotal, user, startDate, endDate, period);
        }
    }

    private String compareVacationDaysAndReservedDays(VacationTotalModel vacationTotal, UserModel user,
                                                      LocalDate startDate, LocalDate endDate, int period) {
        String response;
        if (vacationTotal.getVacationTotal() >= period) {
            vacationTotal.setVacationTotal(vacationTotal.getVacationTotal() - period);

            getVacationTotalRepository().save(vacationTotal);

            createVacationModel(user, startDate, endDate, period);
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

    private void createVacationModel( UserModel user, LocalDate startDate, LocalDate endDate, int period ) {
        VacationModel vacationModel = new VacationModel();
        vacationModel.setUserId(user.getId());
        vacationModel.setDialogId(String.valueOf(UUID.randomUUID()));
        vacationModel.setDays(period);
        LocalTime localTime = LocalTime.now();
        vacationModel.setStartDate( LocalDateTime.of( startDate, localTime )
                .atZone( ZoneId.systemDefault() ).toInstant().toEpochMilli() );
        vacationModel.setEndDate( LocalDateTime.of( endDate, localTime )
                .atZone( ZoneId.systemDefault() ).toInstant().toEpochMilli() );
        getVacationModelRepository().save( vacationModel );
    }
}
