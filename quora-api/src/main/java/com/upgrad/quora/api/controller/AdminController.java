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

    @RequestMapping(method = RequestMethod.DELETE, path = "/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@RequestHeader("authorization") final String authorization, @RequestParam("userId") final String uuid) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity deletedUser = adminService.deleteUser(authorization, uuid);

        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(deletedUser.getUuid()).status("SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
    }
}
