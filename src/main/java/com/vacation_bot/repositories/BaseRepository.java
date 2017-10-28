package com.vacation_bot.repositories;

import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Base class for custom repositories.
 */
public abstract class BaseRepository {

    protected final MongoOperations mongoOperations;

    protected BaseRepository( MongoOperations aMongoOperations ) {
        mongoOperations = aMongoOperations;
    }
}
