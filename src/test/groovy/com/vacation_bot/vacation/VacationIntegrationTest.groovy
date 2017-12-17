package com.vacation_bot.vacation

import com.vacation_bot.AbstractSpockIntegrationTest
import com.vacation_bot.domain.models.UserModel
import com.vacation_bot.domain.models.VacationRequestBody
import com.vacation_bot.domain.models.VacationTotalModel
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

/**
 * Integration level test of {@Link VacationGateway}.
 */
class VacationIntegrationTest extends AbstractSpockIntegrationTest{

    def restTemplate = new TestRestTemplate()

    def headers = new HttpHeaders(accept: Arrays.asList(MediaType.APPLICATION_JSON))

    String userId = UUID.randomUUID()
    def userName = 'Alex'
    def aliases = 'Batman, Joker, Superman'
    def startDate = '2017-10-02'
    def endDate = '2017-10-15'

    def parsedStartDate = LocalDate.parse(startDate)
    def parsedEndDate = LocalDate.parse(endDate)
    String period = ChronoUnit.DAYS.between(parsedStartDate, parsedEndDate)

    def validUser = new UserModel( id: userId, name: userName, aliases: aliases.split(', '))
    def inputData = new VacationRequestBody(userName: userName, startDate: startDate, endDate: endDate)

    def static final defaultVacationTotalDays = 20
    def static final noVacationTotalDays = 0

    def daysLeft = defaultVacationTotalDays - Integer.valueOf(period)

    def vacationTotalDays = 10
    def vacationTotalYear = 2017
    def validVacationTotal = new VacationTotalModel( userId: userId, vacationTotal: vacationTotalDays, year: vacationTotalYear)

    final String vacationReserved = "The registration of your vacation from ${startDate} to ${endDate} was successfully completed! You have left ${daysLeft} days"
    final String vacationNotReserved = "You can not receive vacation. You have ${validVacationTotal.getVacationTotal()} days."
    final String noVacationDays = "You can not receive vacation. You have no vacation"

    def 'exercise controller (vacation total is null)'() {

        given: 'the user is saved to the db'
        userRepository.save(validUser)

        when: 'the controller is called'
        String result = getResponse(inputData).getBody()

        then: 'the result is correct'
        result == vacationReserved
        def createdVacationTotal = vacationTotalRepository.findByUserId(userId)
        createdVacationTotal != null
        createdVacationTotal.getVacationTotal() == daysLeft

        def createdVacation = vacationModelRepository.findByUserId(userId)
        LocalDateTime.ofInstant(Instant.ofEpochMilli(createdVacation.getStartDate()), ZoneOffset.UTC).toLocalDate() == parsedStartDate
        LocalDateTime.ofInstant(Instant.ofEpochMilli(createdVacation.getEndDate()), ZoneOffset.UTC).toLocalDate() == parsedEndDate

    }

    def 'exercise controller (vacation total is not null)'() {

        given: 'the vacation total is saved to the db'
        vacationTotalRepository.save(validVacationTotal)

        and: 'the user is saved to the db'
        userRepository.save(validUser)

        when: 'the controller is called'
        String result = getResponse(inputData).getBody()

        then: 'result is correct'
        result == vacationNotReserved
        validVacationTotal.getVacationTotal() == vacationTotalDays
        vacationModelRepository.findAll() == []

    }

    def 'exercise controller (reserve by aliases)'() {
        given: 'set aliases in input data '
        inputData.setUserName(aliases)

        and: 'and change vacation total days'
        validVacationTotal.setVacationTotal(noVacationTotalDays)

        and: 'the vacation total is saved to the db'
        vacationTotalRepository.save(validVacationTotal)

        and: 'the user is saved to the db'
        userRepository.save(validUser)

        when: 'the controller is called'
        String result = getResponse(inputData).getBody()

        then: 'result is correct'
        result == noVacationDays
        vacationModelRepository.findAll() == []

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
