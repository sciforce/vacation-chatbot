package com.vacation_bot.core.process

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.domain.CustomizedSentence
import com.vacation_bot.domain.models.VacationModel
import com.vacation_bot.repositories.DefaultRepositoryFactory
import com.vacation_bot.repositories.VacationModelRepository
import org.springframework.data.domain.Pageable

/**
 * The unit level testing of the {@link RequestVacationListService}.
 */
class RequestVacationListServiceUnitTest extends AbstractSpockUnitTest {

    def vacationModelRepository = Mock( VacationModelRepository )
    def factory = new DefaultRepositoryFactory( [vacationModelRepository] )
    def sut = new RequestVacationListService( factory )

    def vacation1StartDate = '10.10.18'
    def vacation1EndDate = '15.10.18'
    def vacation2StartDate = '10.11.18'
    def vacation2EndDate = '15.11.18'
    def expectedVacations = [
            new VacationModel( startDate: RequestVacationListService.format.parse( vacation1StartDate ).getTime(),
                    endDate: RequestVacationListService.format.parse( vacation1EndDate ).getTime() ),
            new VacationModel( startDate: RequestVacationListService.format.parse( vacation2StartDate ).getTime(),
                    endDate: RequestVacationListService.format.parse( vacation2EndDate ).getTime() )
    ]

    def expectedResponse = "Your vacations: \nfrom ${vacation1StartDate} to ${vacation1EndDate}\nfrom ${vacation2StartDate} to ${vacation2EndDate}\n"

    def 'exercise list vacations in time period'() {
        given: 'valid customization sentence'
        def startDate = '01.10.18'
        def stopDate = '01.12.18'
        def userExternalCode = 'UserName'
        def parsedStartDate = RequestVacationListService.format.parse( startDate ).getTime()
        def parsedStopDate = RequestVacationListService.format.parse( stopDate ).getTime()
        def customizationSentence = new CustomizedSentence( userExternalCode: userExternalCode, dates: [startDate, stopDate] )

        when: 'the exercised method is called'
        def result = sut.retrieveVacationList( customizationSentence )

        then: 'vacations are retrieved as expected'
        1 * vacationModelRepository
                .findAllByUserIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual( userExternalCode, parsedStartDate, parsedStopDate ) >> expectedVacations

        and: 'the response are constructed as expected'
        result.currentResponse == expectedResponse
    }

    def 'exercise list vacations in time period with no vacations in the system'() {
        given: 'valid customization sentence'
        def startDate = '01.10.18'
        def stopDate = '01.12.18'
        def userExternalCode = 'UserName'
        def parsedStartDate = RequestVacationListService.format.parse( startDate ).getTime()
        def parsedStopDate = RequestVacationListService.format.parse( stopDate ).getTime()
        def customizationSentence = new CustomizedSentence( userExternalCode: userExternalCode, dates: [startDate, stopDate] )

        when: 'the exercised method is called'
        def result = sut.retrieveVacationList( customizationSentence )

        then: 'no vacations are retrieved'
        1 * vacationModelRepository
                .findAllByUserIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual( userExternalCode, parsedStartDate, parsedStopDate ) >> []

        and: 'the response are constructed as expected'
        result.currentResponse == 'You had no vacations in the specified period.'
    }

    def 'exercise list specified amount of vacations'() {
        given: 'valid customization sentence'
        def userExternalCode = 'UserName'
        def vacationsAmount = 3
        def customizationSentence = new CustomizedSentence( userExternalCode: userExternalCode, numbers: [vacationsAmount.toString()] )

        when: 'the exercised method is called'
        def result = sut.retrieveVacationList( customizationSentence )

        then: 'vacations are retrieved as expected'
        1 * vacationModelRepository.findAllByUserId( userExternalCode, !null as Pageable ) >> expectedVacations

        and: 'the response are constructed as expected'
        result.currentResponse == expectedResponse
    }

    def 'exercise list specified amount of vacations with no vacations in the database'() {
        given: 'valid customization sentence'
        def userExternalCode = 'UserName'
        def vacationsAmount = 3
        def customizationSentence = new CustomizedSentence( userExternalCode: userExternalCode, numbers: [vacationsAmount.toString()] )

        when: 'the exercised method is called'
        def result = sut.retrieveVacationList( customizationSentence )

        then: 'vacations are retrieved as expected'
        1 * vacationModelRepository.findAllByUserId( userExternalCode, !null as Pageable ) >> []

        and: 'the response are constructed as expected'
        result.currentResponse == 'You had no vacations in the specified period.'
    }
}
