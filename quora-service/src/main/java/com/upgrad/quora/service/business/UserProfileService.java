package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserDao userDao;

    /**
     * Service method to view a user
     *
     * signed in user can view any user details
     *
     * @param authorization authorization of the signed in user from header
     * @param uuid user id
     *
     * @exception AuthorizationFailedException
     * @exception UserNotFoundException
     *
     * @return details of the user
     * */
    public UserEntity viewUser(final String authorization, final String uuid) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        UserEntity userEntity = userDao.getUserByUUID(uuid);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }
        return userEntity;
    }
}
