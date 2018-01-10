package com.vacation_bot.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Represents requested vacation.
 */
@Getter
@Setter
@EqualsAndHashCode
public class VacationRequestBody {

    /**
     * The name of user who request vacation.
     */
    @JsonProperty(required = true)
    private String userName;

    /**
     * The start date of requested vacation.
     */
    @JsonProperty(required = true)
    private String startDate;

    /**
     * The end date of requested vacation.
     */
    @JsonProperty(required = true)
    private String endDate;

}
