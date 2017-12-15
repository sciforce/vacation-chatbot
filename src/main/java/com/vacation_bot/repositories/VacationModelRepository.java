package com.vacation_bot.repositories;

import com.vacation_bot.domain.models.VacationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Talks to {@link VacationModel} collection in the database.
 */
public interface VacationModelRepository extends MongoRepository<VacationModel, String> {

    VacationModel findByUserId(String userId);

}
