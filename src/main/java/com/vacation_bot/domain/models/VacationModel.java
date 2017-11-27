package com.vacation_bot.domain.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Represents the operation of vacation reservation.
 */
@Setter
@Getter
@EqualsAndHashCode
@Document( collection = "vacation" )
public class VacationModel {

    /**
     * Database generated primary key of the document and also the vacation unique identifier.
     */
    @Id
    @Field( FieldNames.ID )
    private String id;

    /**
     * The user identifier who reserves vacation.
     */
    @Field( FieldNames.USER_ID )
    private String userId;

    /**
     * The dialog based on which the vacation was reserved.
     */
    @Field( FieldNames.DIALOG_ID )
    private String dialogId;

    /**
     * The amount of reserved days.
     */
    @Field( FieldNames.DAYS )
    private int days;

    /**
     * The start date of vacations.
     */
    @Field( FieldNames.START_DATE )
    private long startDate;

    /**
     * The end date of vacations.
     */
    @Field( FieldNames.END_DATE )
    private long endDate;

    public static final class FieldNames {
        public static final String ID = "_id";
        public static final String USER_ID = "user-id";
        public static final String DIALOG_ID = "dialog-id";
        public static final String DAYS = "days";
        public static final String START_DATE = "start-date";
        public static final String END_DATE = "end-date";
    }
}
