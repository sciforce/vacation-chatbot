package com.vacation_bot.vacation

import com.vacation_bot.AbstractSpockIntegrationTest
import com.vacation_bot.domain.models.UserModel
import com.vacation_bot.domain.models.VacationRequestBody
import com.vacation_bot.domain.models.VacationTotalModel
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import spock.lang.Shared

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

    String userId = UUID.randomUUID()
    def validUser = new UserModel( id: userId, name: 'Alex', aliases: [])

    def inputData =  new VacationRequestBody(userName: 'Alex', startDate: '2017-10-02', endDate: '2017-10-15')

    static final String vacationReserved = "The registration of your vacation from 2017-10-02 to 2017-10-15 was successfully completed! You have left 7 days"
    static final String vacationNotReserved = "You can not receive vacation. You have 10 days."

    def 'exercise createVacation (vacation total is null)'() {

        given: 'the user is saved to the db'
        userRepository.save(validUser)

        and: 'set headers'
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))

        when: 'the controller is called'
        String result = getResponse(inputData).getBody()

        then: 'the result is correct'
        result == vacationReserved
        vacationTotalRepository.findByUserId(userId) != null
        vacationTotalRepository.findByUserId(userId).getVacationTotal() == 7
        LocalDateTime.ofInstant(Instant.ofEpochMilli(vacationModelRepository.findByUserId(userId).getStartDate()), ZoneOffset.UTC).toLocalDate() == LocalDate.of(2017, 10, 02)
        LocalDateTime.ofInstant(Instant.ofEpochMilli(vacationModelRepository.findByUserId(userId).getEndDate()), ZoneOffset.UTC).toLocalDate() == LocalDate.of(2017, 10,15)

    }

    def 'exercise createVacation (vacation total is not null)'() {

        given: 'given valid vacation total'
        String vacationTotalId = UUID.randomUUID()
        def validVacationTotal = new VacationTotalModel(id: vacationTotalId,
                userId: userId, vacationTotal: 10, year: 2017)

        and: 'the vacation total is saved to the db'
        vacationTotalRepository.save(validVacationTotal)

        and: 'the user is saved to the db'
        userRepository.save(validUser)

        and: 'set headers'
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))

        when: 'the controller is called'
        String result = getResponse(inputData).getBody()

        then: 'result is correct'
        result == vacationNotReserved

    }

    private ResponseEntity<String> getResponse(VacationRequestBody inputData) {
        HttpEntity<VacationRequestBody> entity = new HttpEntity<VacationRequestBody>(inputData, headers)
        return restTemplate.exchange(createURLWithPort('/api/vacation'),
                HttpMethod.POST, entity, String.class)
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri
    }


}
