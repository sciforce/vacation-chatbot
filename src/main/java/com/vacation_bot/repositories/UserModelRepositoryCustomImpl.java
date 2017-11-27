package com.vacation_bot.repositories;

import com.vacation_bot.domain.models.UserModel;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;

import java.util.ArrayList;
import java.util.List;

import static com.vacation_bot.domain.models.UserModel.FieldNames.ALIASES;
import static com.vacation_bot.domain.models.UserModel.FieldNames.NAME;

/**
 * The custom implementation of mongo repositories.
 */
public class UserModelRepositoryCustomImpl extends BaseRepository implements UserModelRepositoryCustom {

    public UserModelRepositoryCustomImpl( MongoOperations aMongoOperations ) {
        super( aMongoOperations );
    }

    @Override
    public List<String> retrieveAllUserNamesAndAliases() {
        TypedAggregation<UserModel> aggregation = Aggregation.newAggregation( UserModel.class,
                Aggregation.unwind( ALIASES, true ),
                Aggregation.group().addToSet( NAME ).as( NAME ).addToSet( ALIASES ).as( ALIASES ),
                Aggregation.project().and( NAME ).unionArrays( ALIASES ).as( ALIASES ) );
        UserModel result = mongoOperations.aggregate( aggregation, UserModel.class ).getUniqueMappedResult();
        return result != null ? result.getAliases() : new ArrayList<>();
    }
}
