package com.vacation_bot.repositories;

import com.vacation_bot.domain.models.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Talks to {@link UserModel} collection in the database.
 */
public interface UserModelRepository extends MongoRepository<UserModel, String>, UserModelRepositoryCustom {

    UserModel findByNameOrAliases(String name, List<String> aliases);

}
