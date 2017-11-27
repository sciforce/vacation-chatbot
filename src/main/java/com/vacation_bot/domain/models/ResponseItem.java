package com.vacation_bot.domain.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
public class ResponseItem {

    @Field( FieldNames.OK )
    private List<String> okResponses;

    @Field( FieldNames.FAIL )
    private List<String> failResponses;

    public static final class FieldNames {
        public static final String OK = "ok";
        public static final String FAIL = "fail";
    }
}
