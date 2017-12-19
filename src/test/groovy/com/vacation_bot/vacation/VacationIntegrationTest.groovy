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

    def vacationTotalDays = 18
    def vacationTotalYear = 2017
    def validVacationTotal = new VacationTotalModel(userId: userId, vacationTotal: vacationTotalDays, year: vacationTotalYear)

    def 'exercise controller (vacation total is null)'() {

        given: 'the user is saved to the db'
        userRepository.save(validUser)

        and: 'set new end date'
        inputData.endDate = newEndDate

        when: 'the controller is called'
        String result = getResponse(inputData).getBody()

        then: 'the result is correct'
        result == expectedResult
        def createdVacationTotal = vacationTotalRepository.findAll().stream().filter { e -> e.userId == userId && e.year == vacationTotalYear }.findFirst().get()
        createdVacationTotal
        createdVacationTotal.vacationTotal == expectedVacationTotalDays

        def createdVacation = vacationModelRepository.findAll().stream().filter { e -> e.userId == userId }.findFirst()
        def createdVacationPresent = createdVacation.isPresent()
        createdVacationPresent == expectedCreatedVacation
//        with(createdVacationPresent) {
//            LocalDateTime.ofInstant(Instant.ofEpochMilli(createdVacation.get().startDate), ZoneOffset.UTC).toLocalDate() == parsedStartDate
//            LocalDateTime.ofInstant(Instant.ofEpochMilli(createdVacation.get().endDate), ZoneOffset.UTC).toLocalDate() == LocalDate.parse(newEndDate)
//        }

        where:
        newEndDate      ||  expectedCreatedVacation     ||  expectedVacationTotalDays                                                                           ||  expectedResult
        '2017-10-15'    ||  true                        ||  defaultVacationTotalDays - ChronoUnit.DAYS.between(parsedStartDate, LocalDate.parse(newEndDate))    ||  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You have left ${defaultVacationTotalDays - ChronoUnit.DAYS.between(parsedStartDate, LocalDate.parse(newEndDate))} days"
        '2017-10-22'    ||  true                        ||  defaultVacationTotalDays - ChronoUnit.DAYS.between(parsedStartDate, LocalDate.parse(newEndDate))    ||  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You don't have vacation days"
        '2017-10-25'    ||  false                       ||  20                                                                                                  ||  "You can not receive vacation. You have ${availableDays} days."
    }

    def 'exercise controller (vacation total is not null)'() {

        given: 'the vacation total is saved to the db'
        validVacationTotal.vacationTotal = newVacationTotalDays
        vacationTotalRepository.save(validVacationTotal)

        and: 'the user is saved to the db'
        userRepository.save(validUser)

        and: 'set new end date'
        inputData.endDate = newEndDate

        when: 'the controller is called'
        String result = getResponse(inputData).getBody()

        then: 'result is correct'
        result == expectedResult
        validVacationTotal.vacationTotal == newVacationTotalDays
        vacationModelRepository.findAll().isEmpty() == expectedVacationList

        where:
        newEndDate      ||  newVacationTotalDays    ||  expectedVacationList    ||  expectedResult
        '2017-10-15'    ||  20                      ||  false                    ||  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You have left ${newVacationTotalDays - ChronoUnit.DAYS.between(parsedStartDate, LocalDate.parse(newEndDate))} days"
        '2017-10-18'    ||  16                      ||  false                    ||  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You don't have vacation days"
        '2017-10-25'    ||  5                       ||  true                   ||  "You can not receive vacation. You have ${newVacationTotalDays} days."
        '2017-10-25'    ||  0                       ||  true                   ||  "You can not receive vacation. You have no vacation"

    }

    def 'exercise controller (reserve by aliases)'() {
        given: 'set aliases in input data '
        inputData.setUserName(aliases[0])

        and: 'and change vacation total days'
        validVacationTotal.setVacationTotal(newVacationTotalDays)

        and: 'the vacation total is saved to the db'
        vacationTotalRepository.save(validVacationTotal)

        and: 'the user is saved to the db'
        userRepository.save(validUser)

        and: 'set new end date'
        inputData.endDate = newEndDate

        when: 'the controller is called'
        String result = getResponse(inputData).getBody()

        then: 'result is correct'
        result == expectedResult
        vacationModelRepository.findAll().isEmpty() == expectedVacationList

        where:
        newEndDate      ||  newVacationTotalDays    ||  expectedVacationList    ||  expectedResult
        '2017-10-15'    ||  20                      ||  false                    ||  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You have left ${newVacationTotalDays - ChronoUnit.DAYS.between(parsedStartDate, LocalDate.parse(newEndDate))} days"
        '2017-10-18'    ||  16                      ||  false                    ||  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You don't have vacation days"
        '2017-10-25'    ||  5                       ||  true                   ||  "You can not receive vacation. You have ${newVacationTotalDays} days."
        '2017-10-25'    ||  0                       ||  true                   ||  "You can not receive vacation. You have no vacation"

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
