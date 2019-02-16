package com.vacation_bot.core.process

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.domain.CustomizedSentence
import com.vacation_bot.domain.models.UserModel
import com.vacation_bot.domain.models.VacationTotalModel
import com.vacation_bot.repositories.DefaultRepositoryFactory
import com.vacation_bot.repositories.UserModelRepository
import com.vacation_bot.repositories.VacationTotalModelRepository
import spock.lang.Shared

/**
 * The unit level testing of the {@link RequestDaysLeftService}.
 */
class RequestDaysLeftServiceUnitTest extends AbstractSpockUnitTest {

    def inputUserName = 'Alex'

    @Shared
    def validUser = Optional.of( new UserModel( id: '1', name: 'Alex', email: 'alex@mail.com' ) )

    @Shared
    def validVacationTotal = new VacationTotalModel( userId: validUser.get().getId(), vacationTotal: 15, year: 2017 )

    def 'exercise request days left'() {
        given: 'valid subject under test'
        def userModelRepository = Mock( UserModelRepository )
        def vacationTotalRepository = Mock( VacationTotalModelRepository )
        def factory = new DefaultRepositoryFactory( [userModelRepository, vacationTotalRepository] )
        def sut = new RequestDaysLeftService( factory )

        and: 'input customization sentence'
        def customizationSentence = new CustomizedSentence( persons: [inputUserName] )

        when: 'the exercised method is called'
        def result = sut.calculateDaysLeft( customizationSentence )

        then: 'the use is retrieved from the system'
        1 * userModelRepository.findById( inputUserName ) >> validUser

        and: 'the totals are retrieved from the system'
        1 * vacationTotalRepository.findByUserIdAndYear( validUser.get().id, Calendar.getInstance().get( Calendar.YEAR ) ) >> validVacationTotal

        and: 'the response are constructed as expected'
        result.currentResponse == "You still have " + validVacationTotal.getVacationTotal() + " days left."
    }
}
