package com.vacation_bot.repositories;

import com.vacation_bot.domain.models.DialogModel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Talks to {@link DialogModel} collection in the database.
 */
public interface DialogModelRepository extends MongoRepository<DialogModel, String> { }
