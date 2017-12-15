package com.vacation_bot.domain.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Represents the operation of vacation total reservation.
 */
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "vacation_total")
@CompoundIndex(name = "unique_idx", def = "{ 'user-id': 1, 'year': 1 }", unique = true)
public class VacationTotalModel {

    /**
     * Database generated primary key of the document.
     */
    @Id
    @Field(FieldNames.ID)
    private String id;

    /**
     * The user unique identifier.
     */
    @Field(FieldNames.USER_ID)
    private String userId;

    /**
     * The total amount of valid vacation days that the user still have this year.
     */
    @Field(FieldNames.VACATION_TOTAL)
    private int vacationTotal;

    /**
     * The year this document belongs to.
     */
    @Field(FieldNames.YEAR)
    private int year;

    public static class FieldNames {
        public static final String ID = "_id";
        public static final String USER_ID = "user-id";
        public static final String VACATION_TOTAL = "vacation-total";
        public static final String YEAR = "year";
    }

}
