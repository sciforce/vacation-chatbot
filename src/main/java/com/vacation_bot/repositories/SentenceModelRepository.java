package com.vacation_bot.repositories;

import com.vacation_bot.domain.models.SentenceModel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Talks to {@link SentenceModel} collection in the database.
 */
public interface SentenceModelRepository extends MongoRepository<SentenceModel, String> { }
