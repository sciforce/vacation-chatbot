package com.vacation_bot.core;

import com.vacation_bot.repositories.RepositoryFactory;
import com.vacation_bot.repositories.SentenceModelRepository;
import com.vacation_bot.repositories.UserModelRepository;

/**
 * Base class for services.
 */
public abstract class BaseService {

    protected final RepositoryFactory repositoryFactory;

    /**
     * Default constructor.
     * @param aRepositoryFactory The repository factory containing the injected repositories needed for communicating to various database documents.
     */
    protected BaseService( RepositoryFactory aRepositoryFactory ) {
        repositoryFactory = aRepositoryFactory;
    }

    protected UserModelRepository getUserModelRepository() {
        return repositoryFactory.getRepository( UserModelRepository.class );
    }

    protected SentenceModelRepository getSentenceModelRepository() {
        return repositoryFactory.getRepository( SentenceModelRepository.class );
    }
}
