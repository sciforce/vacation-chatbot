package com.vacation_bot.domain.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Represents the user entity.
 */
@Getter
@Setter
@EqualsAndHashCode
@Document( collection = "user" )
@CompoundIndex( name = "unique_idx", def = "{ 'user-id': 1, 'year': 1 }", unique = true )
public class UserModel {

    /**
     * Database generated primary key of the document.
     */
    @Id
    @Field( FieldNames.ID )
    public String id;

    /**
     * The user unique identifier.
     */
    @Field( FieldNames.USER_ID )
    public String userId;

    /**
     * The user name.
     */
    @Field( FieldNames.NAME )
    public String name;

    /**
     * The user name aliases.
     */
    @Field( FieldNames.ALIASES )
    public List<String> aliases;

    /**
     * The total amount of valid vacation days that the user still have this year.
     */
    @Field( FieldNames.VACATION_TOTAL )
    public int vacationTotal;

    /**
     * The year this document belongs to.
     */
    @Field( FieldNames.YEAR )
    public int year;

    static final class FieldNames {
        public static final String ID = "_id";
        public static final String USER_ID = "user-id";
        public static final String NAME = "name";
        public static final String ALIASES = "aliases";
        public static final String VACATION_TOTAL = "vacation-total";
        public static final String YEAR = "year";
    }
}
