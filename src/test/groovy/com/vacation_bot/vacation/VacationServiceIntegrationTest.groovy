package com.vacation_bot.vacation

import com.vacation_bot.AbstractSpockIntegrationTest
import com.vacation_bot.domain.models.UserModel
import com.vacation_bot.domain.models.VacationRequestBody
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Integration level test of {@Link VacationService}.
 */
class VacationServiceIntegrationTest extends AbstractSpockIntegrationTest{

    def restTemplate = new TestRestTemplate()

    def headers = new HttpHeaders()

    def 'exercise createVacation'() {

        given: 'given valid input data'
        def inputData = new VacationRequestBody(
                userName: 'Alex',
                startDate: '2017-10-02',
                endDate: '2017-10-15')

        and: 'given valid user'
        String userId = UUID.randomUUID()
        def validUser = new UserModel( id: userId, name: 'Alex', aliases: [])

        and: 'the user is saved to the db'
        userRepository.save(validUser)

        and: 'set headers'
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))

        when: 'the controller is called'
        HttpEntity<VacationRequestBody> entity = new HttpEntity<VacationRequestBody>(inputData, headers)

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort('/api/vacation'),
                HttpMethod.POST, entity, String.class
        )

        String result = response.getBody()

        then: 'the result is correct'
        result == "The registration of your vacation from 2017-10-02 to 2017-10-15 was successfully completed! You have left 7 days"
        vacationTotalRepository.findByUserId(userId) != null
        vacationTotalRepository.findByUserId(userId).getVacationTotal() == 7
        LocalDateTime.ofInstant(Instant.ofEpochMilli(vacationModelRepository.findByUserId(userId).getStartDate()), ZoneOffset.UTC).toLocalDate() == LocalDate.of(2017, 10, 02)
        LocalDateTime.ofInstant(Instant.ofEpochMilli(vacationModelRepository.findByUserId(userId).getEndDate()), ZoneOffset.UTC).toLocalDate() == LocalDate.of(2017, 10,15)
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri
    }


}
