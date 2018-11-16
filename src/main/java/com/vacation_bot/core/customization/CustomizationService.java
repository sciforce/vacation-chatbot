package com.vacation_bot.core.customization;

import com.vacation_bot.core.BaseService;
import com.vacation_bot.domain.CustomizedSentence;
import com.vacation_bot.repositories.RepositoryFactory;
import com.vacation_bot.shared.logging.VacationBotLoggingMessages;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Customize the incoming sentence.
 */
//TODO: understand day, week, month, year... write numbers
public class CustomizationService extends BaseService {

    private static final String STAGE = "customization";

    private static final String DATE_TOKEN = "_date";

    private static final String NUMBER_TOKEN = "_num";

    private static final String PERSON_TOKEN = "_person";

    public CustomizationService( RepositoryFactory repositoryFactory ) {
        super( repositoryFactory );
    }

    @ServiceActivator
    public Message<CustomizedSentence> customizeSentence( Message<CustomizedSentence> message ) {
        getLogger().info( VacationBotLoggingMessages.STAGE_LOGGING.getMessage(), STAGE );
        CustomizedSentence customizedSentence = message.getPayload();
        String updatedSentence = customizedSentence.getOriginalSentence().toLowerCase();

        // first find dates in the string. We should look for dates before removing the punctuation.
        updatedSentence = findDates( updatedSentence, customizedSentence ); // set the result into object

        updatedSentence = updatedSentence.replaceAll("\\p{P}", "" ); //removes punctuation

        // and replace with _DATE token
        updatedSentence = replaceDatesWithToken( updatedSentence, customizedSentence );

        // then find all other numbers in string and replace with _NUM token
        updatedSentence = findAndReplaceNumbers( updatedSentence, customizedSentence ); // set results into object

        // and finally find name by registered user and replace with _PERSON token
        updatedSentence = findAndReplacePersonNames( updatedSentence, customizedSentence );
        customizedSentence.setCustomizedSentence( Arrays.asList( updatedSentence.split( " " ) ) );

        //TODO: decode words abreviations into full words. I'm not sure what abreviations we should support. Will be added later.

        return MessageBuilder.withPayload( customizedSentence ).copyHeaders( message.getHeaders() ).build();
    }

    private String findDates( String sentence, CustomizedSentence customizedSentence ) {
        List<String> matchedDates = new ArrayList<>();
        Matcher matcher = Pattern.compile( "(0[1-9]|1[012])[- \\/.](0[1-9]|[12][0-9]|3[01])[- \\/.](17|18|19|20)" )
                .matcher( sentence );
        while ( matcher.find() ) {
            String finding = matcher.group();
            matchedDates.add( finding );
        }
        customizedSentence.setDates( matchedDates );
        return sentence;
    }

    private String replaceDatesWithToken( String sentence, CustomizedSentence customizedSentence ) {
        for ( String date : customizedSentence.getDates() ) {
            String dateWithoutPunctuation = date.replaceAll( "\\p{P}", "" );
            sentence = sentence.replaceAll( dateWithoutPunctuation, DATE_TOKEN );
        }
        return sentence;
    }

    private String findAndReplaceNumbers( String sentence, CustomizedSentence customizedSentence ) {
        String justNumbers = sentence.replaceAll( "[^0-9]+", " " );
        //TODO: regex for write numbers
        List<String> results = justNumbers.equals( " " ) ? new ArrayList<>() : Arrays.asList( justNumbers.trim().split( " " ) );
        for ( String num : results ) {
            sentence = sentence.replaceAll( num, NUMBER_TOKEN );
        }
        customizedSentence.setNumbers( results );
        return sentence;
    }

    private String findAndReplacePersonNames( String sentence, CustomizedSentence customizedSentence ) {
        // retrieve from the database all users names + aliases
        List<String> availableNames = getUserModelRepository().retrieveAllUserNamesAndAliases();
        List<String> foundNames = new ArrayList<>();
        if ( availableNames != null ) {
            for ( String name : availableNames ) {
                if ( sentence.contains( name.toLowerCase() ) ) {
                    foundNames.add(name);
                    sentence = sentence.replaceAll( name.toLowerCase(), PERSON_TOKEN );
                }
            }
        }
        //TODO: what to do with duplicated names? For right now lets imagine that names are unique. It looks like we should find a way to set here the userId
        // It should be possible to retrieve userId directly from Slack
        customizedSentence.setPersons( foundNames );
        return sentence;
    }
}
