package com.vacation_bot

import com.vacation_bot.repositories.UserModelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * The base class of integration spock tests.
 */
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.NONE )
@ContextConfiguration( classes = VacationBotApplication )
abstract class AbstractSpockIntegrationTest extends Specification {

    @Autowired
    MongoOperations mongoTemplate

    @Autowired
    UserModelRepository userRepository

    def setup() {
        assert mongoTemplate
        resetMongoDatabase()
    }

    protected void resetMongoDatabase() {
        mongoTemplate.collectionNames.findAll { !it.startsWith( 'system.' ) }.each {
            mongoTemplate.remove( new Query(), it )
        }
    }
}
