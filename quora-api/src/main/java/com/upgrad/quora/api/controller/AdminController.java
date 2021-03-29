package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;

import com.upgrad.quora.service.business.AdminService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Controller method to delete a user
     *
     * calls the deleteUser of the admin service
     *
     * @param authorization authorization of the signed in user from header
     * @param uuid user id of the user that needs to be deleted
     *
     * @exception AuthorizationFailedException
     * @exception UserNotFoundException
     *
     * @return  the 200 status code with the user id of the user with status SUCCESSFULLY DELETED
     * */
    @RequestMapping(method = RequestMethod.DELETE, path = "/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@RequestHeader("authorization") final String authorization, @RequestParam("userId") final String uuid) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity deletedUser = adminService.deleteUser(authorization, uuid);

        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(deletedUser.getUuid()).status("SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
    }
}
