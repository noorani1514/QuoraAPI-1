package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBusinessService {

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

        //check if the user name already exists or not if exists then throw an error
        UserEntity userByUserName = userDao.getUserByUserName(userEntity.getUserName());
        if (userByUserName != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }

        //check if the email id already exists or not if exists then throw an error
        UserEntity userByEmailID = userDao.getUserByEmailId(userEntity.getEmail());
        if (userByEmailID != null) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }

        //Encrypt the password and get the encrypted salt and password
        String encryptedPassword[] = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        //store the encrypted salt
        userEntity.setSalt(encryptedPassword[0]);
        //store thr encrypted password
        userEntity.setPassword(encryptedPassword[1]);

        //persist the user entity
        return userDao.createUser(userEntity);
    }
}
