package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {

    @Autowired
    private UserDao userDao;

    private UserEntity userEntity;
    @RequestMapping(value = "/deleteImage", method = RequestMethod.DELETE)

    public UserEntity userDelete(final UserEntity userEntity, @PathVariable("userId") final String id, String uuid, final String authorization) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getAuthtoken(authorization);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userDao.getUserById((id))!=userEntity){
            throw new AuthorizationFailedException ("ATHR-002","User is signed out");
        }

        String role = userEntity.getRole();

        if (role !=("admin")) {

            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");

        }
            UserEntity existinguserEntity = userDao.getUserById(uuid);


            if (existinguserEntity == null) {
                throw new AuthorizationFailedException("USR-001", "User with entered uuid to be deleted does not exist");
            }


        else {


             ResponseEntity.status(HttpStatus.valueOf("DELETE"));
                    return userDao.getUserById(uuid);


        }
    }
}
