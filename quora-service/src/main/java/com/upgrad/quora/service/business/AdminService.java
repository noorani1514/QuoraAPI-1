package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AdminDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AdminDao adminDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUser(final String authorization, final String uuid) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);
        UserEntity loggedUser = userDao.getUserByUUID(userAuthEntity.getUser().getUuid());
        if(loggedUser.getRole().equals("admin") || loggedUser.getRole().equals("ADMIN") || loggedUser.getRole().equals("Admin")) {
            UserEntity deleteUser = userDao.getUserByUUID(uuid);
            if(deleteUser == null) {
                throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
            }
            return adminDao.deleteUser(deleteUser);

        }
        else {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }
    }
}
