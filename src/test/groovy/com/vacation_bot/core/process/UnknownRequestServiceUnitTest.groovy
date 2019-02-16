package com.vacation_bot.core.process

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.domain.CustomizedSentence
import com.vacation_bot.repositories.DefaultRepositoryFactory

/**
 * The unit level testing of the {@link UnknownRequestService}.
 */
class UnknownRequestServiceUnitTest extends AbstractSpockUnitTest {

    def 'exercise process of unknown request'() {
        given: 'valid subject under test'
        def factory = new DefaultRepositoryFactory( [] )
        def sut = new UnknownRequestService( factory )

        when: 'the exercised method is called'
        def result = sut.handleRequest( new CustomizedSentence() )

        then: 'the expected response is constructed'
        result.currentResponse == "Remember that I'm only a bot. I just understand things related to vacations. Please repeat :)"
    }
}
