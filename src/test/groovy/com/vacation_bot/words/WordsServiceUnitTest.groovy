package com.vacation_bot.words

import com.vacation_bot.AbstractSpockUnitTest
import com.vacation_bot.core.words.WordsService
import com.vacation_bot.domain.models.SentenceModel
import com.vacation_bot.repositories.DefaultRepositoryFactory
import com.vacation_bot.repositories.SentenceModelRepository
import com.vacation_bot.shared.SentenceClass

class WordsServiceUnitTest extends AbstractSpockUnitTest {

    def 'exercise words extraction'() {
        given: 'valid subject under test'
        def sentenceRepository = Mock( SentenceModelRepository )
        def factory = new DefaultRepositoryFactory( [sentenceRepository] )
        def sut = new WordsService( factory )

        and: 'valid documents'
        def documents = [new SentenceModel().with {
            type = SentenceClass.REGISTER_VACATION
            examples = ['Register a vacation for _num days.', 'I want to take a vacation for one week.']
            it
        },
        new SentenceModel().with {
            type = SentenceClass.EDIT_VACATION
            examples = ['Cancel my registration.', 'I wrote wrong dates.']
            it
        }]

        when: 'the exercised method is called'
        def result = sut.calculateWordsWorth()

        then: 'the repository is called as expected'
        1 * sentenceRepository.findAll() >> documents

        and: 'the result is returned as expected'
        result.keySet().size() == documents.size()
    }
}
