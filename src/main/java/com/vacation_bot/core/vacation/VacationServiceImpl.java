package com.vacation_bot.core.vacation;

import com.vacation_bot.domain.models.UserModel;
import com.vacation_bot.domain.models.VacationModel;
import com.vacation_bot.domain.models.VacationTotal;
import com.vacation_bot.repositories.UserModelRepository;
import com.vacation_bot.repositories.VacationModelRepository;
import com.vacation_bot.repositories.VacationTotalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

@Service
public class VacationServiceImpl implements VacationService{

    private VacationModelRepository vacationModelRepository;

    private VacationTotalRepository vacationTotalRepository;

    private UserModelRepository userModelRepository;

    public VacationServiceImpl(VacationModelRepository vacationModelRepository,
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

        //found user by name
        UserModel user = userModelRepository.findByName(userName);

        // find vacation total by user and current year
        VacationTotal vacationTotal = vacationTotalRepository
                .findByUserIdAndYear(user.getId(), Calendar.getInstance().get(Calendar.YEAR));

        //check if vacationTotal is null. if it isn't, create new vacation total and vacation model.
        if (vacationTotal == null) {

            VacationTotal newVacationTotal = new VacationTotal();
            newVacationTotal.setUserId(user.getId());
            //set default total of days
            newVacationTotal.setVacationTotal(31);
            newVacationTotal.setYear(Calendar.getInstance().get(Calendar.YEAR));

            vacationTotalRepository.save(newVacationTotal);

            if (period.getYears() == 0 && period.getMonths() == 0) {

                createVacationModel(user, startDate, endDate, period);

                return "The registration of your vacation from" + startDate + " to " + endDate +
                        " was successfully completed! You still have " + newVacationTotal.getVacationTotal() + " days";
            } else {
                return "You don't have enough days to vacation. You just have "
                        + newVacationTotal.getVacationTotal() + " days.";
            }

        } else {

            //if vacationTotal not null check dates
            if (period.getYears() == 0 && period.getMonths() == 0 &&
                    vacationTotal.getVacationTotal() >= period.getDays()) {

                vacationTotal.setVacationTotal(vacationTotal.getVacationTotal() - period.getDays());
                vacationTotalRepository.save(vacationTotal);

                createVacationModel(user, startDate, endDate, period);

                return "The registration of your vacation from" + startDate + " to " + endDate +
                        " was successfully completed! You still have " + vacationTotal.getVacationTotal() + " days";

            } else {
                return "You don't have enough days to vacation. You just have "
                        + vacationTotal.getVacationTotal() + " days.";
            }

        }

    }

    private void createVacationModel(UserModel user, LocalDate startDate, LocalDate endDate, Period period) {

        VacationModel vacationModel = new VacationModel();
        vacationModel.setUserId(user.getId());
        vacationModel.setDialogId(String.valueOf((int)Math.random()*100));
        vacationModel.setDays(period.getDays());

        //parse to string to stick together year, month and day
        String strStartDate = String.valueOf(startDate.getYear()) +
                String.valueOf(startDate.getMonthValue()) + String.valueOf(startDate.getDayOfMonth());

        vacationModel.setStartDate(Long.parseLong(strStartDate));

        String strEndDate = String.valueOf(endDate.getYear()) +
                String.valueOf(endDate.getMonthValue()) + String.valueOf(endDate.getDayOfMonth());

        vacationModel.setEndDate(Long.parseLong(strEndDate));

        vacationModelRepository.save(vacationModel);

    }

}
