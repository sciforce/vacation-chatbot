package com.vacation_bot.repositories;

import com.vacation_bot.domain.models.VacationTotalModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VacationTotalModelRepository extends MongoRepository<VacationTotalModel, String> {
}
