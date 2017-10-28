package com.vacation_bot.core.classification;

import com.vacation_bot.core.customization.CustomizedSentence;
import com.vacation_bot.shared.MessageHeaders;
import com.vacation_bot.shared.SentenceClass;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Classify the input customized sentence.
 */
public class ClassificationService {

    private final static List<String> REQUEST_INFO_WORDS = Arrays.asList( "days", "how", "many", "left", "have", "has", "still", "much", "any",  "does", "do" );

    private final static List<String> LIST_VACATION_WORDS = Arrays.asList( "show", "list", "last", "all" );

    private final static List<String> REGISTER_VACATION_WORDS = Arrays.asList( "need", "want", "take", "can", "day", "off", "from", "till", "register" );

    private final static List<String> EDIT_VACATION_WORDS = Arrays.asList( "want", "change", "shift", "move", "can", "edit", "correct" );

    private final static List<String> CANCEL_WORDS = Arrays.asList( "cancel", "undo", "forgot", "wrong", "no" );

    @ServiceActivator
    public Message<CustomizedSentence> classifySentence( Message<CustomizedSentence> message ) {
        SentenceClass classResult = classify( message.getPayload().getCustomizedSentence() );

        return MessageBuilder.fromMessage( message )
                .setHeader( MessageHeaders.SENTENCE_CLASS_HEADER, classResult.toString() )
                .build();
    }

    private SentenceClass classify( List<String> sentenceWords ) {
        SentenceClass classResult = SentenceClass.UNKNOWN;
        int matches = 0;

        int classOneCount = countCommonElements( sentenceWords, REQUEST_INFO_WORDS );
        int classTwoCount = countCommonElements( sentenceWords, LIST_VACATION_WORDS );
        int classThreeCount = countCommonElements( sentenceWords, REGISTER_VACATION_WORDS );
        int classFourCount = countCommonElements( sentenceWords, EDIT_VACATION_WORDS );
        int classFiveCount = countCommonElements( sentenceWords, CANCEL_WORDS );

        if ( classOneCount > matches ) {
            classResult = SentenceClass.REQUEST_DAYS_LEFT;
            matches = classOneCount;
        }
        if ( classTwoCount > matches ) {
            classResult = SentenceClass.REQUEST_VACATION_LIST;
            matches = classTwoCount;
        }
        if ( classThreeCount > matches ) {
            classResult = SentenceClass.REGISTER_VACATION;
            matches = classThreeCount;
        }
        if ( classFourCount > matches ) {
            classResult = SentenceClass.EDIT_VACATION;
            matches = classFourCount;
        }
        if ( classFiveCount > matches ) {
            classResult = SentenceClass.CANCEL_CURRENT_OPERATION;
        }

        return classResult;
    }

    private int countCommonElements( List<String> sentenceWords, List<String> libraryWords ) {
        return sentenceWords.stream().filter( libraryWords::contains ).collect( toList() ).size();
    }
}
