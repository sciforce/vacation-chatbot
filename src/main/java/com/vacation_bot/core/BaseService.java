package com.vacation_bot.core;

import com.vacation_bot.repositories.*;

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

    protected VacationModelRepository getVacationModelRepository() {
        return repositoryFactory.getRepository( VacationModelRepository.class );
    }

    protected VacationTotalRepository getVacationTotalRepository() {
        return repositoryFactory.getRepository( VacationTotalRepository.class );
    }

}
