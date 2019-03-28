package com.vacation_bot.core.validation

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.domain.CustomizedSentence
import spock.lang.Shared
import spock.lang.Unroll

/**
 * The unit level testing of the {@link RequestVacationListValidator}.
 */
class RequestVacationListValidatorUnitTest extends AbstractSpockUnitTest {

    @Shared
    def responseOfInvalidRequest = 'Please provide the requested period one more time or amount of vacations. The example format: from dd.MM.yy to dd.MM.yy'

    @Unroll
    def 'exercise #description request filtering'() {
        given: 'valid subject under test'
        def sut = new RequestVacationListValidator()

        when: 'the exercised method is called'
        def result = sut.validateRequest( customizedSentence )

        then: 'the request is validated as expected'
        result == isValid
        customizedSentence.currentResponse == expectedResponse

        where:
        customizedSentence                                                          || isValid | expectedResponse         | description
        new CustomizedSentence( dates: [], numbers: ['1'] )                         || true    | null                     | 'valid with numbers'
        new CustomizedSentence( dates: ['01.01.18', '01.03.18'], numbers: [] )      || true    | null                     | 'valid with dated'
        new CustomizedSentence( dates: ['01.01.18', '01.03.18'], numbers: ['1'] )   || true    | null                     | 'valid with numbers and dated'
        new CustomizedSentence( dates: [], numbers: [] )                            || false   | responseOfInvalidRequest | 'invalid'
    }
}
