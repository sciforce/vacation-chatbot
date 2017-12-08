package com.vacation_bot.vacation

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.core.vacation.VacationServiceImpl
import com.vacation_bot.domain.models.UserModel
import com.vacation_bot.repositories.UserModelRepository


class VacationServiceUnitTest extends AbstractSpockUnitTest{

    def 'exercise createVacation'() {
        given: 'given valid subject under test'
        def vc = new VacationServiceImpl()
        def ur = Mock(UserModelRepository)

        and: 'given valid object to set into db'
        def user = new UserModel(
                name: 'Alex',
                email: 'alex@mail.com'
        )

        and: 'save object to db'
        ur.save(user)

        and: 'valid input data'
        def userName = 'Alex'
        def startDate = '2017-10-02'
        def endDate = '2017-10-20'

        when: 'the exercised method is called'
        def result = vc.createVacation(userName, startDate, endDate)

        then: 'result'
        result.contains('successfully completed!')

    }

}
