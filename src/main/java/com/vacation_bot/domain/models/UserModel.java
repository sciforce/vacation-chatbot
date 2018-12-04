package com.vacation_bot.domain.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
public class UserModel {

    /**
     * Database generated primary key of the document.
     */
    @Id
    @Field( FieldNames.ID )
    private String id;

    /**
     * The user name.
     */
    @Field( FieldNames.NAME )
    @Indexed
    private String name;

    /**
     * The user email.
     */
    @Field(FieldNames.EMAIL)
    private String email;

    /**
     * The user name aliases.
     */
    @Field( FieldNames.ALIASES )
    @Indexed
    private List<String> aliases;

    public static final class FieldNames {
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String ALIASES = "aliases";
        public static final String EMAIL = "email";
    }
}
