package com.vacation_bot.customization

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.core.customization.CustomizationService
import com.vacation_bot.repositories.DefaultRepositoryFactory
import com.vacation_bot.repositories.UserModelRepository
import org.springframework.integration.support.MessageBuilder
import spock.lang.Unroll

/**
 * Unit level testing for {@link CustomizationService}
 */
class CustomizationServiceUnitTest extends AbstractSpockUnitTest {

    @Unroll
    def 'exercise sentence customization'() {
        given: 'valid subject under test'
        def userRepository = Mock( UserModelRepository )
        def repositoryFactory = new DefaultRepositoryFactory( [userRepository] )
        def sut = new CustomizationService( repositoryFactory )

        and: 'valid input message'
        def input = MessageBuilder.withPayload( inputSentence ).build()

        when: 'the exercise method is called'
        def result = sut.customizeSentence( input )

        then: 'the sentence is customized as expected'
        1 * userRepository.retrieveAllUserNamesAndAliases() >> ['James Bond']

        and: 'the sentence is customizes as expected'
        with ( result.payload ) {
            customizedSentence == resultSentence.split( ' ' ) as List<String>
            originalSentence == inputSentence
            dates == expectedDates
            numbers == expectedNumbers
            persons == expectedNames
        }
        result.payload.customizedSentence == resultSentence.split( ' ' ) as List<String>
        // check numbers, dates and persons

        where:
        inputSentence                                                   || resultSentence                                               | expectedDates             | expectedNumbers       | expectedNames
        'How many days left for me?'                                    || 'how many days left for me'                                  | []                        | []                    | []
        'How many days does James Bond have?'                           || 'how many days does _PERSON have'                            | []                        | []                    | ['James Bond']
        'Can I take a vacation for one week starting from 12.01.19'     || 'can i take a vacation for one week starting from _DATE'     | ['12.01.19']              | []                    | []
        'Show me my last 3 vacation reservation?'                       || 'show me my last _NUM vacation reservation'                  | []                        | ['3']                 | []
        'Please register a vacation from 01.04.17 to 01.10.19'          || 'please register a vacation from _DATE to _DATE'             | ['01.04.17', '01.10.19']  | []                    | []
    }
}
