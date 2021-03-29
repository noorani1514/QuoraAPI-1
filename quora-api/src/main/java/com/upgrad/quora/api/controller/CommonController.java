package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserProfileService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userprofile")
public class CommonController {

    @Autowired
    private UserProfileService userProfileService;


    /**
     * Get the user details provided the userId.
     *
     * @param authorization Authorization token to authenticate the user who is requesting for user details.
     * @param uuid UUID of the user to get details
     * @throws AuthorizationFailedException - if the access token is invalid or already logged out.
     * @throws UserNotFoundException - if the user with given id is not present in the database.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> viewUser(@RequestHeader("authorization") final String authorization, @RequestParam("userId") final String uuid) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity userEntity = userProfileService.viewUser(authorization, uuid);

        UserDetailsResponse response = new UserDetailsResponse()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .userName(userEntity.getUserName())
                .emailAddress(userEntity.getEmail())
                .country(userEntity.getCountry())
                .aboutMe(userEntity.getAboutMe())
                .dob(userEntity.getDob())
                .contactNumber(userEntity.getContactNumber());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
