package com.vacation_bot.gateway.inbound;

import com.vacation_bot.core.vacation.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vacation")
@CrossOrigin("*")
public class VacationGateway {

    @Autowired
    private VacationService vacationService;

    @PostMapping()
    public String getResponse(@RequestPart("user-name") String userName,
                              @RequestPart("start-date") String startDate,
                              @RequestPart("end-date") String endDate){
        String response = vacationService.createVacation(userName, startDate, endDate);
        return response;
    }

}
