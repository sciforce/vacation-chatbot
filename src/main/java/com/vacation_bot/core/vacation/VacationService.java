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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

/**
 * Reserve a vacation to the user.
 */
@Service
public class VacationService extends BaseService{

    private static final int DEFAULT_VACATION_TOTAL_DAYS = 20;

    private static final String USER_NOT_FOUND_MESSAGE = "USER NOT FOUND";

    private int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public VacationService(RepositoryFactory factory) {
        super(factory);
    }

    public String createVacation(final String userName, final String _startDate, final String _endDate) {

        LocalDate startDate = LocalDate.parse(_startDate);
        LocalDate endDate = LocalDate.parse(_endDate);

        //the days reserved by user
        int period = (int) ChronoUnit.DAYS.between(startDate, endDate);

        //found user by name or aliases
        UserModel user = getUserModelRepository().findByNameOrAliasesIn(userName, Arrays.asList(userName))
                .orElseThrow(() -> new RepositoryException(USER_NOT_FOUND_MESSAGE));


        // find vacation total by user and current year
        VacationTotalModel vacationTotal = getVacationTotalRepository()
                .findByUserIdAndYear(user.getId(), currentYear);

        //check if vacationTotal is null. if it isn't, create new vacation total and vacation model.
        if (vacationTotal == null) {

            VacationTotalModel newVacationTotal = new VacationTotalModel();
            newVacationTotal.setUserId(user.getId());

            //set default total of days
            newVacationTotal.setVacationTotal(DEFAULT_VACATION_TOTAL_DAYS);
            newVacationTotal.setYear(currentYear);

            return compareVacationDaysAndReservedDays(newVacationTotal, user, startDate, endDate, period);

        } else {

            //if vacationTotal not null check dates
            return compareVacationDaysAndReservedDays(vacationTotal, user, startDate, endDate, period);

        }

    }

    private String compareVacationDaysAndReservedDays(VacationTotalModel vacationTotal, UserModel user,
                                                      LocalDate startDate, LocalDate endDate, int period) {
        if (vacationTotal.getVacationTotal() >= period) {

            vacationTotal.setVacationTotal(vacationTotal.getVacationTotal() - period);
            getVacationTotalRepository().save(vacationTotal);

            createVacationModel(user, startDate, endDate, period);

            return "The registration of your vacation from " + startDate + " to " + endDate +
                    " was successfully completed! You have left " + vacationTotal.getVacationTotal() + " days";

        } else {
            if (vacationTotal.getVacationTotal() > 0) {
                return "You can not receive vacation. You have "
                        + vacationTotal.getVacationTotal() + " days.";
            } else {
                return  "You can not receive vacation. You have no vacation";
            }
        }
    }

    private void createVacationModel(UserModel user, LocalDate startDate, LocalDate endDate, int period) {

        VacationModel vacationModel = new VacationModel();
        vacationModel.setUserId(user.getId());
        vacationModel.setDialogId(String.valueOf(UUID.randomUUID()));
        vacationModel.setDays(period);

        LocalTime localTime = LocalTime.now();

        vacationModel.setStartDate(
                LocalDateTime.of(startDate, localTime).
                        atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        );

        vacationModel.setEndDate(
                LocalDateTime.of(endDate, localTime).
                        atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        );

        getVacationModelRepository().save(vacationModel);

    }

}
