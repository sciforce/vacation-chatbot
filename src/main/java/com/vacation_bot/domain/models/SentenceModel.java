package com.vacation_bot.domain.models;

import com.vacation_bot.shared.SentenceClass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sentence entity.
 */
@Setter
@Getter
@EqualsAndHashCode
@Document( collection = "sentence" )
public class SentenceModel {

    /**
     * Database generated primary key of the document and also the dialog unique identifier.
     */
    @Id
    @Field( FieldNames.ID ) // it cannot override the _id name. But now we can clearly use constant as field name instead of string.
    private String id;

    @Field( FieldNames.TYPE )
    private SentenceClass type;

    @Field( FieldNames.EXAMPLES )
    private List<String> examples;

    @Field( FieldNames.RESPONSES )
    private ArrayList<ResponseItem> responses;

    public static final class FieldNames {
        public static final String ID = "_id";
        public static final String TYPE = "type";
        public static final String EXAMPLES = "examples";
        public static final String RESPONSES = "responses";
    }
}
