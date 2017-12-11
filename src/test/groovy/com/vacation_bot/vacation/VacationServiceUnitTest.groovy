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


class VacationServiceUnitTest extends AbstractSpockUnitTest{

    def 'exercise createVacation'() {
        given: 'given valid subject under test'
        def userModelRepository = Mock(UserModelRepository)
        def vacationTotalRepository = Mock(VacationTotalRepository)
        def vacationModelRepository = Mock(VacationModelRepository)

        def factory = new DefaultRepositoryFactory([userModelRepository, vacationTotalRepository, vacationModelRepository])
        def vacationService = new VacationService(factory)

        and: 'valid input data'
        def userName = 'Alex'
        def startDate = '2017-10-02'
        def endDate = '2017-10-20'

        when: 'the exercised method is called when user is null'
        vacationService.createVacation(userName, startDate, endDate)

        then: 'repositories return expected exception'
        1 * userModelRepository.findByNameOrAliases(userName, Arrays.asList(userName)) >> null

        thrown(RepositoryException)

        when: 'the exercised method is called'
        def result2 = vacationService.createVacation(userName, startDate, endDate)

        then: 'repositories return expected values'
        1 * userModelRepository.findByNameOrAliases(user.getName(), Arrays.asList(user.getName())) >> user
        1 * vacationTotalRepository.findByUserIdAndYear(user.getId(), 2017) >> vacationTotal

        result2 == expectedResult

        where:
        user                                                            ||   vacationTotal                                                               ||  expectedResult
        new UserModel(id: '1', name: 'Alex', email: 'alex@mail.com')    ||   new VacationTotalModel(userId: user.getId(), vacationTotal: 15, year: 2017) ||  "You can not receive vacation. You have 15 days."
        new UserModel(id: '1', name: 'Alex', email: 'alex@mail.com')    ||   new VacationTotalModel(userId: user.getId(), vacationTotal: 25, year: 2017) ||  "The registration of your vacation from 2017-10-02 to 2017-10-20 was successfully completed! You have left 7 days"
        new UserModel(id: '1', name: 'Alex', email: 'alex@mail.com')    ||   null                                                                        ||  "The registration of your vacation from 2017-10-02 to 2017-10-20 was successfully completed! You have left 2 days"
    }

}
