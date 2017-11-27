package com.vacation_bot.repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Default implementation of the repository factory.
 */
public class DefaultRepositoryFactory implements RepositoryFactory {

    /**
     * The available mongo repositories that interact with MongoDB.
     */
    private final List<MongoRepository> repositories;

    public DefaultRepositoryFactory( final List<MongoRepository> someRepositories ) {
        repositories = someRepositories;
    }

    @Override
    @Cacheable( "mongo-repositories" )
    public <T extends MongoRepository> T getRepository( Class<T> clazz ) {
        return (T) repositories.stream()
                .filter( item -> clazz.isAssignableFrom( item.getClass() ) )
                .findFirst()
                .orElseThrow( () -> new RuntimeException( "Repository not found error." ) );
    }
}
