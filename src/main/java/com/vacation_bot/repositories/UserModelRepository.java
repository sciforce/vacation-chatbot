package com.vacation_bot.repositories;

import com.vacation_bot.domain.models.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Talks to {@link UserModel} collection in the database.
 */
public interface UserModelRepository extends MongoRepository<UserModel, String>, UserModelRepositoryCustom {

    Optional<UserModel> findByNameOrAliases(String name, String alias);


}
