package com.vacation_bot.vacation

import com.vacation_bot.AbstractSpockIntegrationTest
import com.vacation_bot.domain.models.UserModel
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

/**
 * Integration level test of {@Link VacationService}.
 */
class VacationServiceIntegrationTest extends AbstractSpockIntegrationTest{

    def restTemplate = new TestRestTemplate()

    def headers = new HttpHeaders()

    def 'exercise createVacation'() {

        given: 'given valid input data'
        def inputData = "{\"user-name\":\"Alex\"," +
                "\"start-date\":\"2017-10-02\"," +
                "\"end-date\": \"2017-10-23\"}"

        and: 'set headers'
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))

        and: 'given valid user'
        def validUser = new UserModel( id: UUID.randomUUID(),
                name: 'Alex',
                aliases: [])

        and: 'the user is saved to the db'
        userRepository.save(validUser)

        when: 'the controller is called'
        def entity = new HttpEntity(inputData, headers)

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/vacation"),
                HttpMethod.POST, entity, String.class)

        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0)

        then: 'the result is correct'
        //response == 'You can not receive vacation. You have 15 days.'
        actual.contains("/api/vacation")

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:8080" + uri
    }


}
