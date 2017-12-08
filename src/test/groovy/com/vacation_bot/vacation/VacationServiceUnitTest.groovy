package com.vacation_bot.vacation

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.core.vacation.VacationServiceImpl
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

        def vacationService = new VacationServiceImpl(vacationModelRepository,
                vacationTotalRepository, userModelRepository)


        and: 'given valid user object '
        def user = new UserModel(id: '1', name: 'Alex', email: 'alex@mail.com')

        and: 'given valid vacation object'
        def vacationTotal1 = new VacationTotal(userId: user.getId(), vacationTotal: 15, year: 2017)
        //def vacationTotal2 = new VacationTotal()

        and: 'valid input data'
        def userName = 'Alex'
        def startDate = '2017-10-02'
        def endDate = '2017-10-20'

        when: 'the exercised method is called'
        def result = vacationService.createVacation(userName, startDate, endDate)

        then: 'result'
        1 * userModelRepository.findByName(user.name) >> user
        1 * vacationTotalRepository.findByUserIdAndYear(user.id, 2017) >> vacationTotal1
//        1 * vacationTotalRepository.save(vacationTotal2) >> void

        print result
        !result.isEmpty()

    }

}
