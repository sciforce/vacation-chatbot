package com.vacation_bot.core.classification;

import com.vacation_bot.core.customization.CustomizedSentence;
import com.vacation_bot.core.words.WordsService;
import com.vacation_bot.shared.MessageHeaders;
import com.vacation_bot.shared.SentenceClass;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Classify the input customized sentence.
 */
public class ClassificationService {

    private final WordsService wordsService;

    public ClassificationService( WordsService aWordsService ) {
        wordsService = aWordsService;
    }

    @ServiceActivator
    public Message<CustomizedSentence> classifySentence( Message<CustomizedSentence> message ) {
        SentenceClass classResult = processSentence( message.getPayload().getCustomizedSentence() );

        return MessageBuilder.fromMessage( message )
                .setHeader( MessageHeaders.SENTENCE_CLASS_HEADER, classResult.toString() )
                .build();
    }

    private SentenceClass processSentence(List<String> sentenceWords ) {
        Map<String, List<String>> wordsPerSentenceType = wordsService.calculateWordsWorth();

        SentenceClass classResult = SentenceClass.UNKNOWN;
        int matches = 0;

        int classOneCount = countCommonElements( sentenceWords, wordsPerSentenceType.get( SentenceClass.REQUEST_DAYS_LEFT.toString() ) );
        int classTwoCount = countCommonElements( sentenceWords, wordsPerSentenceType.get( SentenceClass.REQUEST_VACATION_LIST.toString() ) );
        int classThreeCount = countCommonElements( sentenceWords, wordsPerSentenceType.get( SentenceClass.REGISTER_VACATION.toString() ) );
        int classFourCount = countCommonElements( sentenceWords, wordsPerSentenceType.get( SentenceClass.EDIT_VACATION.toString() ) );
        int classFiveCount = countCommonElements( sentenceWords, wordsPerSentenceType.get( SentenceClass.CANCEL_CURRENT_OPERATION.toString() ) );

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
