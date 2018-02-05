package com.vacation_bot.classification

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.UnitTest
import com.vacation_bot.core.classification.ClassificationService
import com.vacation_bot.core.customization.CustomizedSentence
import com.vacation_bot.core.words.WordsService
import com.vacation_bot.shared.MessageHeaders
import com.vacation_bot.shared.SentenceClass
import org.junit.experimental.categories.Category
import org.springframework.integration.support.MessageBuilder

/**
 * Unit level test for {@link ClassificationService}.
 */
@Category( UnitTest )
class ClassificationServiceUnitTest extends AbstractSpockUnitTest {

    def 'exercise sentence classification'() {
        given: 'valid subject under test'
        def wordsService = Mock( WordsService )
        def sut = new ClassificationService( wordsService )

        and: 'valid input sentence'
        def inputSentence = 'I would like take a vacation'
        def inputPayload = new CustomizedSentence( customizedSentence: inputSentence.split( ' ' ) )
        def input = MessageBuilder.withPayload( inputPayload ).build()

        and: 'valid classification words'
        def expectedClass = SentenceClass.REQUEST_DAYS_LEFT.toString()
        def wordsMap = [( expectedClass ): inputSentence.split( ' ' ) as List<String>,
                        ( SentenceClass.REQUEST_VACATION_LIST.toString() ): ['i', 'want', 'cancel', 'registration'],
                        ( SentenceClass.REGISTER_VACATION.toString() ): ['some', 'random', 'words'],
                        ( SentenceClass.EDIT_VACATION.toString() ): ['some', 'random', 'words'],
                        ( SentenceClass.CANCEL_CURRENT_OPERATION.toString() ): ['some', 'random', 'words']]

        when: 'the exercised method is called'
        def result = sut.classifySentence( input )

        then: 'the sentence was classified right'
        1 * wordsService.calculateWordsWorth() >> wordsMap
        result
        result.headers.get( MessageHeaders.SENTENCE_CLASS_HEADER ) == expectedClass
    }
}
