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


    @Shared
    def startDate = '2017-10-02'

    @Shared
    def endDate = '2017-10-26'

    @Shared
    def static final DEFAULT_VACATION_TOTAL_DAYS = 20

    static final String USER_ID = UUID.randomUUID()
    def static final USER_NAME = 'Alex'
    def static final ALIASES = ['Batman', 'Joker', 'Superman']

    def inputData = new VacationRequestBody(userName: USER_NAME, startDate: startDate, endDate: endDate)

    static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR)

    @Unroll
    def 'exercise vacation gateway (vacation total is null)'() {

        given: 'valid user'
        def validUser = new UserModel(id: USER_ID, name: USER_NAME, aliases: ALIASES)

        and: 'the user is saved to the db'
        userRepository.save(validUser)

        and: 'set new end date'
        inputData.endDate = newEndDate

        when: 'the controller is called'
        String result = getResponse(inputData).body

        then: 'the result is correct'
        result == expectedResult
        def createdVacationTotals = vacationTotalRepository.findAll()
        1 == createdVacationTotals.size()
        createdVacationTotals.first().vacationTotal == expectedVacationTotalDays

        def createdUser = userRepository.findAll().first()
        def vacationList = vacationModelRepository.findAll()
        with(vacationList) {
            if (vacationList) {
                def firstVacation = vacationList.first()

                USER_ID == createdUser.id
                firstVacation.days == ChronoUnit.DAYS.between(LocalDate.parse(this.startDate), LocalDate.parse(newEndDate))
                LocalDateTime.ofInstant(Instant.ofEpochMilli(firstVacation.startDate), ZoneOffset.UTC).toLocalDate() == LocalDate.parse(this.startDate)
                LocalDateTime.ofInstant(Instant.ofEpochMilli(firstVacation.endDate), ZoneOffset.UTC).toLocalDate() == LocalDate.parse(newEndDate)
            }
        }

        where:
        newEndDate      |   expectedVacationTotalDays              |  expectedResult
        '2017-10-15'    |   getExpectedVacationDays(newEndDate)    |  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You have left ${getExpectedVacationDays(newEndDate)} days"
        '2017-10-22'    |   getExpectedVacationDays(newEndDate)    |  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You don't have vacation days"
        '2017-10-25'    |   DEFAULT_VACATION_TOTAL_DAYS            |  "You can not receive vacation. You have ${expectedVacationTotalDays} days."
    }

    @Unroll
    def 'exercise vacation gateway (vacation total is not null)'() {

        given: 'valid user'
        def validUser = new UserModel(id: USER_ID, name: USER_NAME, aliases: ALIASES)

        and: 'given vacation total'
        def validVacationTotal = new VacationTotalModel(userId: USER_ID, vacationTotal: newVacationTotalDays, year: CURRENT_YEAR)

        and: 'the vacation total is saved to the db'
        vacationTotalRepository.save(validVacationTotal)

        and: 'the user is saved to the db'
        userRepository.save(validUser)

        and: 'set new end date'
        inputData.endDate = newEndDate

        when: 'the controller is called'
        String result = getResponse(inputData).body

        then: 'result is correct'
        result == expectedResult
        validVacationTotal.vacationTotal == newVacationTotalDays
        vacationModelRepository.findAll().isEmpty() == expectedVacationList

        where:
        newVacationTotalDays    ||  expectedVacationList        |  newEndDate    ||  expectedResult
        20                      ||  false                       |  '2017-10-15'  ||  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You have left ${getExpectedVacationDays(newEndDate)} days"
        16                      ||  false                       |  '2017-10-18'  ||  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You don't have vacation days"
        5                       ||  true                        |  '2017-10-25'  ||  "You can not receive vacation. You have ${newVacationTotalDays} days."
        0                       ||  true                        |  '2017-10-25'  ||  "You can not receive vacation. You have no vacation"

    }

    @Unroll
    def 'exercise vacation gateway (reserve by aliases)'() {
        given: 'set aliases in input data '
        inputData.setUserName(ALIASES[0])

        and: 'given valid user'
        def validUser = new UserModel(id: USER_ID, name: USER_NAME, aliases: ALIASES)

        and: 'the user is saved to the db'
        userRepository.save(validUser)

        and: 'given vacation total'
        def validVacationTotal = new VacationTotalModel(userId: USER_ID, vacationTotal: newVacationTotalDays, year: CURRENT_YEAR)

        and: 'the vacation total is saved to the db'
        vacationTotalRepository.save(validVacationTotal)

        and: 'set new end date'
        inputData.endDate = newEndDate

        when: 'the controller is called'
        String result = getResponse(inputData).body

        then: 'result is correct'
        result == expectedResult
        validVacationTotal.vacationTotal == newVacationTotalDays
        vacationModelRepository.findAll().isEmpty() == expectedVacationList

        where:
        newVacationTotalDays    ||  expectedVacationList        |  newEndDate    ||  expectedResult
        20                      ||  false                       |  '2017-10-15'  ||  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You have left ${getExpectedVacationDays(newEndDate)} days"
        16                      ||  false                       |  '2017-10-18'  ||  "The registration of your vacation from ${startDate} to ${newEndDate} was successfully completed! You don't have vacation days"
        5                       ||  true                        |  '2017-10-25'  ||  "You can not receive vacation. You have ${newVacationTotalDays} days."
        0                       ||  true                        |  '2017-10-25'  ||  "You can not receive vacation. You have no vacation"

    }

    private int getExpectedVacationDays(String endDate) {
        DEFAULT_VACATION_TOTAL_DAYS - ChronoUnit.DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate))
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
