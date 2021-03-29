package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.api.model.SignupUserRequest;

import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    /**
     * This method is for user signup. This method receives the object of SignupUserRequest type with
     * its attributes being set.
     *
     * @param userRequest
     * @return SignupUserResponse - UUID of the user created.
     * @exception SignUpRestrictedException - if the username or email already exist in the database.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest userRequest) throws SignUpRestrictedException {
        /**
         * instance of UserEntity
         */
        UserEntity userEntity = new UserEntity();

        /**
         * provides the values provided by the user in the request to the instance object
        */
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userEntity.setUserName(userRequest.getUserName());
        userEntity.setEmail(userRequest.getEmailAddress());
        userEntity.setPassword(userRequest.getPassword());
        userEntity.setCountry(userRequest.getCountry());
        userEntity.setAboutMe(userRequest.getAboutMe());
        userEntity.setDob(userRequest.getDob());
        userEntity.setRole("nonadmin");
        userEntity.setContactNumber(userRequest.getContactNumber());

        /**
         * pass the user details to the business layer to process
         * */
        final UserEntity createdUser = userBusinessService.signup(userEntity);

        /**
         * create the response
         * */
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUser.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}
