package com.vacation_bot.domain.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Represents the sub-item of dialog model and contains each phrase.
 */
@Setter
@Getter
@EqualsAndHashCode
public class PhraseItem {

    /**
     * The phrase text. Usually one sentence.
     */
    @Field( FieldNames.PHRASE )
    private String phrase;

    /**
     * The date code the phrase war submitted.
     */
    @Field( FieldNames.DATE_CODE )
    private long dateCode;

    public static final class FieldNames {
        public static final String PHRASE = "phrase";
        public static final String DATE_CODE = "date-code";
    }
}
