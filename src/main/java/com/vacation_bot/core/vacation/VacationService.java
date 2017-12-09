package com.vacation_bot.core.vacation;

import com.vacation_bot.domain.models.UserModel;
import com.vacation_bot.domain.models.VacationModel;
import com.vacation_bot.domain.models.VacationTotal;
import com.vacation_bot.repositories.UserModelRepository;
import com.vacation_bot.repositories.VacationModelRepository;
import com.vacation_bot.repositories.VacationTotalRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

//TODO fixes in skype
@Service
public class VacationService implements IVacationService {

    private VacationModelRepository vacationModelRepository;

    private VacationTotalRepository vacationTotalRepository;

    private UserModelRepository userModelRepository;

    private final int DEFAULT_VACATION_TOTAL_DAYS = 20;

    public VacationService(VacationModelRepository vacationModelRepository,
                           VacationTotalRepository vacationTotalRepository,
                           UserModelRepository userModelRepository) {
        this.vacationTotalRepository = vacationTotalRepository;
        this.vacationModelRepository = vacationModelRepository;
        this.userModelRepository = userModelRepository;
    }

    @Override
    public String createVacation(String userName, String start, String end) {

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        //the days reserved by user
        Period period = Period.between(startDate, endDate);

        //found user by name or aliases
        UserModel user = userModelRepository.findByNameOrAliases(userName, Arrays.asList(userName));

        // find vacation total by user and current year
        VacationTotal vacationTotal = vacationTotalRepository
                .findByUserIdAndYear(user.getId(), Calendar.getInstance().get(Calendar.YEAR));

        //check if vacationTotal is null. if it isn't, create new vacation total and vacation model.
        if (vacationTotal == null) {

            VacationTotal newVacationTotal = new VacationTotal();
            newVacationTotal.setUserId(user.getId());
            //set default total of days
            newVacationTotal.setVacationTotal(DEFAULT_VACATION_TOTAL_DAYS);
            newVacationTotal.setYear(Calendar.getInstance().get(Calendar.YEAR));

            vacationTotalRepository.save(newVacationTotal);

            return compareVacationDaysAndReservedDays(newVacationTotal, user, startDate, endDate, period);

        } else {

            //if vacationTotal not null check dates
            return compareVacationDaysAndReservedDays(vacationTotal, user, startDate, endDate, period);

        }

    }

    private String compareVacationDaysAndReservedDays(VacationTotal vacationTotal, UserModel user,
                                                  LocalDate startDate, LocalDate endDate, Period period) {
        if (period.getYears() == 0 && period.getMonths() == 0 &&
                vacationTotal.getVacationTotal() >= period.getDays()) {

            vacationTotal.setVacationTotal(vacationTotal.getVacationTotal() - period.getDays());
            vacationTotalRepository.save(vacationTotal);

            createVacationModel(user, startDate, endDate, period);

            return "The registration of your vacation from " + startDate + " to " + endDate +
                    " was successfully completed! You still have " + vacationTotal.getVacationTotal() + " days";

        } else {
            return "You can not receive vacation. You just have "
                    + vacationTotal.getVacationTotal() + " days.";
        }
    }

    private void createVacationModel(UserModel user, LocalDate startDate, LocalDate endDate, Period period) {

        VacationModel vacationModel = new VacationModel();
        vacationModel.setUserId(user.getId());
        vacationModel.setDialogId(String.valueOf(UUID.randomUUID()));
        vacationModel.setDays(period.getDays());

        LocalTime localTime = LocalTime.now();

        vacationModel.setStartDate(
                LocalDateTime.of(startDate, localTime).
                        atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        );

        vacationModel.setEndDate(
                LocalDateTime.of(endDate, localTime).
                        atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        );

        vacationModelRepository.save(vacationModel);

    }

}
