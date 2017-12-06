package com.vacation_bot.repositories;

import com.vacation_bot.domain.models.VacationTotal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VacationTotalRepository extends MongoRepository<VacationTotal, String> {
}
