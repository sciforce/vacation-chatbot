package com.vacation_bot.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * Represents customized sentence with all retrieved data.
 */
@Setter
@Getter
@EqualsAndHashCode
public class CustomizedSentence {

    /**
     * The user identifier that usually comes from outside.
     */
    private String userExternalCode;

    /**
     * The original input sentence.
     */
    private String originalSentence;

    /**
     * Customized sentence.
     */
    private List<String> customizedSentence = Collections.emptyList();

    /**
     * The current generated response;
     */
    private String currentResponse;

    /**
     * Found dates in the original sentence.
     */
    private List<String> dates = Collections.emptyList();

    /**
     * Found numbers in the original sentence.
     */
    private List<String> numbers = Collections.emptyList();

    /**
     * Found person names in the original sentence.
     */
    private List<String> persons = Collections.emptyList();
}
