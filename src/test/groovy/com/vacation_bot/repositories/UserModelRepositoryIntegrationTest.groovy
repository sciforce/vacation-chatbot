package com.vacation_bot.repositories

import com.vacation_bot.AbstractSpockIntegrationTest
import com.vacation_bot.domain.models.UserModel

/**
 * Integration level test of {@link UserModelRepository} custom implementation.
 */
class UserModelRepositoryIntegrationTest extends AbstractSpockIntegrationTest {

    def 'exercise retrieveAllUserNamesAndAliases'() {
        given: 'valid document'
        def validDoc1 = new UserModel( id: UUID.randomUUID(),
                name: 'James Example',
                aliases: [] )
        def validDoc2 = new UserModel( id: UUID.randomUUID(),
                name: 'Super Man', aliases: ['superMan', 'superM'] )

        and: 'the documents are saved to the database'
        userRepository.save( validDoc1 )
        userRepository.save( validDoc2 )

        when: 'the exercised method is called'
        def result = userRepository.retrieveAllUserNamesAndAliases()

        then: 'the repository returns the expected result'
        result.sort() == ['James Example', 'Super Man', 'superMan', 'superM'].sort()
    }
}
