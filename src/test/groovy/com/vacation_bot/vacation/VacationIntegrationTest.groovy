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
import spock.lang.Shared
import spock.lang.Unroll

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

/**
 * Integration level test of {@Link VacationGateway}.
 */
class VacationIntegrationTest extends AbstractSpockIntegrationTest {

    def restTemplate = new TestRestTemplate()

    def headers = new HttpHeaders(accept: Arrays.asList(MediaType.APPLICATION_JSON))

    String userId = UUID.randomUUID()
    def userName = 'Alex'
    def aliases = ['Batman', 'Joker', 'Superman']
    @Shared
    def startDate = '2017-10-02'
    @Shared
    def endDate = '2017-10-26'

    @Shared
    def parsedStartDate = LocalDate.parse(startDate)
    @Shared
    def parsedEndDate = LocalDate.parse(endDate)
    @Shared
    String period = ChronoUnit.DAYS.between(parsedStartDate, parsedEndDate)

    def validUser = new UserModel(id: userId, name: userName, aliases: aliases)
    def inputData = new VacationRequestBody(userName: userName, startDate: startDate, endDate: endDate)

    @Shared
    def static final defaultVacationTotalDays = 20
    def static final noVacationTotalDays = 0

    @Shared
    def daysLeft = defaultVacationTotalDays - Integer.parseInt(period)

    def vacationTotalDays = 10
    def vacationTotalYear = 2017
    def validVacationTotal = new VacationTotalModel(userId: userId, vacationTotal: vacationTotalDays, year: vacationTotalYear)

    @Shared
    def availableDays = 20

    @Shared
    String vacationReserved = "The registration of your vacation from ${startDate} to ${endDate} was successfully completed! You have left ${daysLeft} days"
    @Shared
    String vacationReservedWithoutLeftDays = "The registration of your vacation from ${startDate} to ${endDate} was successfully completed! You don't have vacation days"
    @Shared
    String vacationNotReserved = "You can not receive vacation. You have ${availableDays} days."
    @Shared
    String noVacationDays = "You can not receive vacation. You have no vacation"

    @Unroll
    def 'exercise controller (vacation total is null)'() {

        given: 'the user is saved to the db'
        userRepository.save(validUser)

        and: 'set new end date'
        endDate = newEndDate

        when: 'the controller is called'
        String result = getResponse(inputData).getBody()

        then: 'the result is correct'
        result == expectedResult
        def createdVacationTotal = vacationTotalRepository.findAll().stream().filter { e -> e.userId == userId && e.year == vacationTotalYear }.findFirst().get()
        createdVacationTotal
        createdVacationTotal.vacationTotal == daysLeft

        def createdVacation = vacationModelRepository.findAll().stream().filter { e -> e.userId == userId }.findFirst().get()
        with(createdVacation) {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(createdVacation.startDate), ZoneOffset.UTC).toLocalDate() == parsedStartDate
            LocalDateTime.ofInstant(Instant.ofEpochMilli(createdVacation.endDate), ZoneOffset.UTC).toLocalDate() == parsedEndDate
        }

        where:
        newEndDate      || expectedResult
        '2017-10-15'    || vacationReserved
        '2017-10-22'    || vacationReservedWithoutLeftDays
        '2017-10-25'    || vacationNotReserved
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
        validVacationTotal.vacationTotal == vacationTotalDays
        vacationModelRepository.findAll() == []

    }

    def 'exercise controller (reserve by aliases)'() {
        given: 'set aliases in input data '
        inputData.setUserName(aliases[0])

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
        def entity = new HttpEntity<VacationRequestBody>(inputData, headers)
        restTemplate.exchange(createURLWithPort('/api/vacation'),
                HttpMethod.POST, entity, String.class)
    }

    private String createURLWithPort(String uri) {
        "http://localhost:" + port + uri
    }


}
