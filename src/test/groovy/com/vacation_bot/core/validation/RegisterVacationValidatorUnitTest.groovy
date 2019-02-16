package com.vacation_bot.core.validation

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.domain.CustomizedSentence
import spock.lang.Shared
import spock.lang.Unroll

/**
 * The unit level testing of the {@link RegisterVacationValidator}.
 */
class RegisterVacationValidatorUnitTest extends AbstractSpockUnitTest {

    @Shared
    def responseOfInvalidRequest = 'I don\'t understand dates you provided. The example format: from dd.MM.yy to dd.MM.yy'

    @Unroll
    def 'exercise #description request filtering'() {
        given: 'valid subject under test'
        def sut = new RegisterVacationValidator()

        when: 'the exercised method is called'
        def result = sut.validateRequest( customizedSentence )

        then: 'the request is validated as expected'
        result == isValid
        customizedSentence.currentResponse == expectedResponse

        where:
        customizedSentence                                          || isValid | expectedResponse
        new CustomizedSentence( dates: [] )                         || false   | responseOfInvalidRequest
        new CustomizedSentence( dates: ['01.01.18', '01.02.18'] )   || true    | null

        description = isValid ? 'valid' : 'invalid'
    }
}
