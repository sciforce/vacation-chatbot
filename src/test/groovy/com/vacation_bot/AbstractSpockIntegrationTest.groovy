package com.vacation_bot

import com.vacation_bot.core.classification.ClassificationService
import com.vacation_bot.core.vacation.VacationService
import com.vacation_bot.repositories.UserModelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
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

    @Autowired
    ClassificationService classificationService

    @Autowired
    VacationService vacationService

    def setup() {
        assert mongoTemplate
        resetMongoDatabase()
    }

    protected void resetMongoDatabase() {
        mongoTemplate.collectionNames.findAll { !it.startsWith( 'system.' ) }.each {
            if ( it != 'sentence' ) { mongoTemplate.remove( new Query(), it ) } // in the sentence collection we pre-populate sentence types with examples
        }
    }
}
