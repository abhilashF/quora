package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminService;
import com.upgrad.quora.service.business.UserService;


import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    //This controller method is called when the request pattern is of type '/admin/user/{userId}'

    @RequestMapping(value = "/admin/user/{userId}", method = RequestMethod.DELETE)

    public ResponseEntity<UserDeleteResponse> userDelete(final UserEntity userEntity, @PathVariable("userId") final Integer id, String uuid) throws AuthorizationFailedException, AuthenticationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = adminService.authenticate(uuid);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userService.getUserById((uuid))!=userEntity){
            throw new AuthorizationFailedException ("ATHR-002","User is signed out");
        }

        String role = userEntity.getRole();

        if (!role.equals("admin")) {

            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");

        }
            UserEntity existinguserEntity = userService.getUserById(uuid);


            if (existinguserEntity == null) {
                throw new AuthorizationFailedException("USR-001", "User with entered uuid to be deleted does not exist");
            }


        else {
                adminService.deleteUser(id);
                final UserEntity deletedUser = adminService.deleteUser(id);
                UserDeleteResponse deleteResponse = new UserDeleteResponse().id(deletedUser.getUuid()).status("USER SUCCESSFULLY DELETED'");

                    return new ResponseEntity<UserDeleteResponse>(deleteResponse, HttpStatus.NO_CONTENT);


        }
    }
}
