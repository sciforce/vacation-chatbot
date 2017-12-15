package com.vacation_bot.domain.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class VacationRequestBody {
    private String userName;
    private String startDate;
    private String endDate;
}
