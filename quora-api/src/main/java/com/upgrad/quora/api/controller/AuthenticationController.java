package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/user")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Controller method to signin a user
     *
     * calls the signin of the authentication service
     *
     * @param authorization authorization of the signed in user from header
     * @exception AuthenticationFailedException
     *
     * @return  the 200 status code with the user id of the user with status SIGNED IN SUCCESSFULLY with the access token in the header
     * */
    @RequestMapping(method = RequestMethod.POST, path = "/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization);
        String decodedText = new String(decode);
        String[] decodedTextWOBasic = decodedText.split(" ");

        UserAuthEntity userAuthEntity = authenticationService.signin(decodedTextWOBasic[1]);

        SigninResponse signinResponse = new SigninResponse().id(userAuthEntity.getUser().getUuid()).message("SIGNED IN SUCCESSFULLY");
        HttpHeaders headers = new HttpHeaders();
        headers.add("access_token", userAuthEntity.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse, headers, HttpStatus.OK);
    }

    /**
     * Controller method to signout a user
     *
     * calls the signout of the authentication service
     *
     * @param authorization authorization of the signed in user from header
     * @exception SignOutRestrictedException
     *
     * @return  the 200 status code with the user id of the user with status SIGNED OUT SUCCESSFULLY
     * */
    @RequestMapping(method = RequestMethod.POST, path = "/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = authenticationService.signout(authorization);
        SignoutResponse signoutResponse = new SignoutResponse().id(userAuthEntity.getUser().getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);
    }
}
