package com.vacation_bot.classification

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.core.classification.ClassificationService
import com.vacation_bot.core.customization.CustomizedSentence
import com.vacation_bot.core.words.WordsService
import com.vacation_bot.shared.MessageHeaders
import org.springframework.integration.support.MessageBuilder

/**
 * Unit level test for {@link ClassificationService}
 */
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
        def wordsMap = [class1: inputSentence.split( ' ' ), class2: ['i', 'want', 'cancel', 'registration']]
        def expectedClass = 'class1'

        when: 'the exercised method is called'
        def result = sut.classifySentence( input )

        then: 'the sentence was classified right'
        1 * wordsService.calculateWordsWorth() >> wordsMap
        result
        result.headers.get( MessageHeaders.SENTENCE_CLASS_HEADER ) == expectedClass
    }
}
