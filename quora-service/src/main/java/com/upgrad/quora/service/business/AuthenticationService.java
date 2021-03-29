package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuthDao userAuthDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signin(final String decodedTextWOBasic) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(decodedTextWOBasic);
        String decodedText = new String(decode);
        String[] decodedCredentials = decodedText.split(":");

        UserEntity userEntity = userDao.getUserByUserName(decodedCredentials[0]);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }
        final String encryptedPassword = PasswordCryptographyProvider.encrypt(decodedCredentials[1], userEntity.getSalt());
        if (encryptedPassword.equals(userEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthEntity userAuthEntity = new UserAuthEntity();
            userAuthEntity.setUuid(UUID.randomUUID().toString());
            userAuthEntity.setUser(userEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthEntity.setExpiresAt(expiresAt.toLocalDateTime());
            userAuthEntity.setLoginAt(now.toLocalDateTime());

            userAuthDao.createUserAuth(userAuthEntity);
            return userAuthEntity;
        }
        else {
            throw new AuthenticationFailedException("ATH-002","Password failed");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED) //To Signout
    public UserAuthEntity signout(final String authorization) throws SignOutRestrictedException, AuthorizationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization);
        String decodedText = new String(decode);
        String[] decodedTextWOBearer = decodedText.split(" ");

        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(decodedTextWOBearer[1]);
//        For test cases please comment the encryption decryption part above that is from line 60 - 64 and
//        uncomment the below line 67
//        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(authorization);
        if (userAuthEntity == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        if(userAuthEntity.getLogoutAt() != null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        LocalDateTime now = LocalDateTime.now();
        userAuthEntity.setLogoutAt(now);
        UserAuthEntity loggedOutUser = userAuthDao.updateLogoutAt(userAuthEntity);
        return loggedOutUser;
    }

    public UserAuthEntity checkAuthentication(final String authorization) throws AuthorizationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization);
        String decodedText = new String(decode);
        String[] decodedTextWOBearer = decodedText.split(" ");

        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(decodedTextWOBearer[1]);
//        For test cases please comment the encryption decryption part above that is from line 82 - 86 and
//        uncomment the below line 89
//        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if(userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        }
        else {
            return userAuthEntity;
        }
    }
}
