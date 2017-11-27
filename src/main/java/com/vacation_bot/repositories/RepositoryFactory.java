package com.vacation_bot.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Provides all of the mongo repositories available to the system.
 */
public interface RepositoryFactory {

    /**
     * Retrieve the specified mongo repository from the system.
     * @param clazz the type of repository to retrieve.
     * @return the mongo repository. If no repository is found, an error is thrown.
     */
    <T extends MongoRepository> T getRepository( Class<T> clazz );
}
