package com.vacation_bot.repositories;

import java.util.List;

/**
 * Custom implementation of the mongo repositories.
 */
public interface UserModelRepositoryCustom {

    /**
     * Retrieved all registered in the system users names and aliases.
     * @return The collection of users names and aliases.
     */
    List<String> retrieveAllUserNamesAndAliases();
}
