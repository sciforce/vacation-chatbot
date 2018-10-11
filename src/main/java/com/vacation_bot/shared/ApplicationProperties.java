package com.vacation_bot.shared;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Custom configuration properties that are driven by Spring Boot and its application.yml file.
 */
@Getter
@Setter
@ConfigurationProperties( value = "vacation-bot" )
public class ApplicationProperties {

    /**
     * Some variable for sample.
     */
    private String foo;
}
