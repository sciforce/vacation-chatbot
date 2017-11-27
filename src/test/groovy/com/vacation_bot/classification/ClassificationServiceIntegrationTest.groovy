package com.vacation_bot.classification

import com.vacation_bot.AbstractSpockIntegrationTest
import com.vacation_bot.core.customization.CustomizedSentence
import com.vacation_bot.shared.MessageHeaders
import com.vacation_bot.shared.SentenceClass
import org.springframework.integration.support.MessageBuilder
import spock.lang.Unroll

class ClassificationServiceIntegrationTest extends AbstractSpockIntegrationTest {

    @Unroll
    def 'exercise classification for #expectedClass'() {
        given: 'valid handler to test'
        assert classificationService

        and: 'valid input sentence'
        def inputPayload = new CustomizedSentence( customizedSentence: inputSentence.split( ' ' ) )
        def input = MessageBuilder.withPayload( inputPayload ).build()

        when: 'the handler is called'
        def result = classificationService.classifySentence( input )

        then: 'the sentence was classified right'
        result
        result.headers.get( MessageHeaders.SENTENCE_CLASS_HEADER ) == expectedClass.toString()

        where:
        inputSentence                                                   || expectedClass
        'how many days left for me'                                     || SentenceClass.REQUEST_DAYS_LEFT
        'do i have any vacation days'                                   || SentenceClass.REQUEST_DAYS_LEFT
        'how many days does _PERSON have'                               || SentenceClass.REQUEST_DAYS_LEFT
        'how many days off do i have'                                   || SentenceClass.REQUEST_DAYS_LEFT
        'can i take a vacation for _NUM week starting from _DATE'       || SentenceClass.REGISTER_VACATION
        'show me my last _NUM vacation reservation'                     || SentenceClass.REQUEST_VACATION_LIST
        'i want to ask for vacation'                                    || SentenceClass.REGISTER_VACATION
        'can i take a day off'                                          || SentenceClass.REGISTER_VACATION
        'i want to change my vacation days'                             || SentenceClass.EDIT_VACATION
        'cancel my request'                                             || SentenceClass.CANCEL_CURRENT_OPERATION
        'something different'                                           || SentenceClass.UNKNOWN
        'oh no it was wrong days'                                       || SentenceClass.CANCEL_CURRENT_OPERATION
        'please register a vacation from _DATE to _DATE'                || SentenceClass.REGISTER_VACATION
    }
}
