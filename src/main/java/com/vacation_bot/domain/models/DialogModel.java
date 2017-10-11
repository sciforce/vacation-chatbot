package com.vacation_bot.domain.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Represents the archived dialog entity.
 */
@Setter
@Getter
@EqualsAndHashCode
@Document( collection = "dialog" )
public class DialogModel {

    /**
     * Database generated primary key of the document and also the dialog unique identifier.
     */
    @Id
    @Field( FieldNames.ID ) // it cannot override the _id name. But now we can clearly use constant as field name instead of string.
    public String id;

    /**
     * The user identified.
     */
    @Field( FieldNames.USER_ID )
    public String userId;

    /**
     * The theme of the dialog.
     */
    @Field( FieldNames.THEME )
    public String theme;

    /**
     * The result status of the dialog.
     */
    @Field( FieldNames.STATUS )
    public DialogStatus status;

    /**
     * The collection of all dialog phrases.
     */
    @Field( FieldNames.PHRASES )
    public List<PhraseItem> phrases;

    static final class FieldNames {
        public static final String ID = "_id";
        public static final String USER_ID = "user-id";
        public static final String THEME = "theme";
        public static final String STATUS = "status";
        public static final String PHRASES = "phrases";
    }
}
