package com.vacation_bot.vacation

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.core.vacation.VacationService
import com.vacation_bot.domain.models.UserModel
import com.vacation_bot.domain.models.VacationTotal
import com.vacation_bot.repositories.UserModelRepository
import com.vacation_bot.repositories.VacationModelRepository
import com.vacation_bot.repositories.VacationTotalRepository


class VacationServiceUnitTest extends AbstractSpockUnitTest{

    def 'exercise createVacation'() {
        given: 'given valid subject under test'
        def userModelRepository = Mock(UserModelRepository)
        def vacationTotalRepository = Mock(VacationTotalRepository)
        def vacationModelRepository = Mock(VacationModelRepository)

        def vacationService = new VacationService(vacationModelRepository,
                vacationTotalRepository, userModelRepository)

        and: 'valid input data'
        def userName = 'Alex'
        def startDate = '2017-10-02'
        def endDate = '2017-10-20'

        when: 'the exercised method is called'
        def result = vacationService.createVacation(userName, startDate, endDate)

        then: 'result'
        1 * userModelRepository.findByNameOrAliases(user.getName(), Arrays.asList(user.getName())) >> user
        1 * vacationTotalRepository.findByUserIdAndYear(user.getId(), 2017) >> vacationTotal

        println result
        result == (expectedResult)

        where:
        user                                                            ||   vacationTotal                                                               ||  expectedResult
        new UserModel(id: '1', name: 'Alex', email: 'alex@mail.com')    ||   new VacationTotal(userId: user.getId(), vacationTotal: 15, year: 2017)      ||  "You can not receive vacation. You just have 15 days."
        new UserModel(id: '1', name: 'Alex', email: 'alex@mail.com')    ||   new VacationTotal(userId: user.getId(), vacationTotal: 25, year: 2017)      ||  "The registration of your vacation from 2017-10-02 to 2017-10-20 was successfully completed! You still have 7 days"
        new UserModel(id: '1', name: 'Alex', email: 'alex@mail.com')    ||   null                                                                        ||  "The registration of your vacation from 2017-10-02 to 2017-10-20 was successfully completed! You still have 2 days"

    }

}
