package com.vacation_bot.gateway.inbound;

import com.vacation_bot.core.vacation.VacationService;
import com.vacation_bot.domain.models.VacationRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vacation")
@CrossOrigin("*")
public class VacationGateway {

    private final VacationService vacationService;

    @Autowired
    public VacationGateway(final VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @PostMapping()
    public String getResponse(@RequestBody VacationRequestBody body){
        String response = vacationService.createVacation(body.getUserName(), body.getStartDate(), body.getEndDate());
        return response;
    }

}
