package com.vacation_bot.vacation

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.core.vacation.VacationService
import com.vacation_bot.domain.models.UserModel
import com.vacation_bot.domain.models.VacationTotalModel
import com.vacation_bot.repositories.DefaultRepositoryFactory
import com.vacation_bot.repositories.UserModelRepository
import com.vacation_bot.repositories.VacationModelRepository
import com.vacation_bot.repositories.VacationTotalRepository
import com.vacation_bot.spring.exception.RepositoryException
import spock.lang.Shared


class VacationServiceUnitTest extends AbstractSpockUnitTest {

    def inputUserName = 'Alex'
    def inputStartDate = '2017-10-02'
    def inputEndDate = '2017-10-20'

    @Shared
    def validUser = Optional.of(new UserModel(id: '1', name: 'Alex', email: 'alex@mail.com'))

    @Shared
    def validVacationTotal1 = new VacationTotalModel(userId: validUser.get().getId(), vacationTotal: 15, year: 2017)

    @Shared
    def validVacationTotal2 = new VacationTotalModel(userId: validUser.get().getId(), vacationTotal: 25, year: 2017)

    def 'exercise createVacation'() {
        given: 'given valid subject under test'
        def userModelRepository = Mock(UserModelRepository)
        def vacationTotalRepository = Mock(VacationTotalRepository)
        def vacationModelRepository = Mock(VacationModelRepository)

        def factory = new DefaultRepositoryFactory([userModelRepository, vacationTotalRepository, vacationModelRepository])
        def vacationService = new VacationService(factory)

        when: 'the exercised method is called'
        def result = vacationService.createVacation(inputUserName, inputStartDate, inputEndDate)

        then: 'repositories return expected values'
        1 * userModelRepository.findByName(inputUserName) >> Optional.ofNullable(null)
        1 * userModelRepository.findByAliases(Arrays.asList(inputUserName)) >> user
        1 * vacationTotalRepository.findByUserIdAndYear(user.get().getId(), 2017) >> vacationTotal

        result == expectedResult

        where:
        user        ||   vacationTotal        ||  expectedResult
        validUser   ||   validVacationTotal1  ||  "You can not receive vacation. You have 15 days."
        validUser   ||   validVacationTotal2  ||  "The registration of your vacation from 2017-10-02 to 2017-10-20 was successfully completed! You have left 7 days"
        validUser   ||   null                 ||  "The registration of your vacation from 2017-10-02 to 2017-10-20 was successfully completed! You have left 2 days"
    }

    def 'exercise createVacation with null'() {
        given:
        def userModelRepository = Mock(UserModelRepository)
        def factory = new DefaultRepositoryFactory([userModelRepository])
        def vacationService = new VacationService(factory)

        when: 'the exercised method is called when user is null'
        vacationService.createVacation(inputUserName, inputStartDate, inputEndDate)

        then: 'repositories return expected exception'
        1 * userModelRepository.findByName(inputUserName) >> Optional.ofNullable(null)
        1 * userModelRepository.findByAliases(Arrays.asList(inputUserName)) >> Optional.ofNullable(null)

        thrown(RepositoryException)
    }

}
