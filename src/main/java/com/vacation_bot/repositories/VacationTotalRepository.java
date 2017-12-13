package com.vacation_bot.repositories;

import com.vacation_bot.domain.models.VacationTotalModel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Talks to {@link VacationTotalModel} collection in the database.
 */
public interface VacationTotalRepository extends MongoRepository<VacationTotalModel, String> {

    VacationTotalModel findByUserIdAndYear(String id, int year);

}
