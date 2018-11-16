package com.vacation_bot.core;

import com.vacation_bot.repositories.*;
import com.vacation_bot.shared.logging.AbstractLoggingAware;

/**
 * Base class for services.
 */
public abstract class BaseService extends AbstractLoggingAware {

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

    protected VacationTotalModelRepository getVacationTotalRepository() {
        return repositoryFactory.getRepository( VacationTotalModelRepository.class );
    }

}
