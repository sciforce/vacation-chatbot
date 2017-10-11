package com.vacation_bot.repositories

import com.vacation_bot.AbstractSpockIntegrationTest
import com.vacation_bot.domain.models.UserModel
import org.springframework.data.domain.Example

class UserModelRepositoryIntegrationTest extends AbstractSpockIntegrationTest {

    def 'exercise the database connection'() {
        given: 'valid document'
        def validDoc = new UserModel().with {
            userId = UUID.randomUUID()
            name = 'James Bond'
            aliases = ['jbond']
            vacationTotal = 20
            year = 2017
            it
        }

        when: 'the repository is called'
        userRepository.save( validDoc )

        then: 'the document is saved to the database'
        userRepository.findOne( Example.of( validDoc ) )
    }
}
