package com.vacation_bot.repositories;

import com.vacation_bot.domain.models.VacationModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Talks to {@link VacationModel} collection in the database.
 */
public interface VacationModelRepository extends MongoRepository<VacationModel, String> {

    List<VacationModel> findAllByUserIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual( final String userId, final long startDate, final long endDate );

    List<VacationModel> findAllByUserId( final String userId, final Pageable page );
}
