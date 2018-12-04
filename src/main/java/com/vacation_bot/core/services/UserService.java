package com.vacation_bot.core.services;

import com.vacation_bot.core.BaseService;
import com.vacation_bot.domain.models.UserModel;
import com.vacation_bot.repositories.RepositoryFactory;
import me.ramswaroop.jbot.core.slack.models.User;

/**
 * Responsible for managing user data.
 */
public class UserService extends BaseService implements UserPort {

    public UserService( final RepositoryFactory factory ) {
        super( factory );
    }

    @Override
    public void save( final User user ) {
        final UserModel userModel = new UserModel();
        userModel.setId( user.getId() );
        userModel.setName( user.getName() );
        userModel.setEmail( user.getProfile().getEmail() );
        getUserModelRepository().save( userModel );
    }
}
