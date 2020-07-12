package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private UserDao userDao;


    @Autowired
    private UserEntity userEntity;
    public UserEntity userProfile(final String id, final String authorization) throws UserNotFoundException, AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getAuthtoken(authorization);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        String role = userEntity.getRole();

            UserEntity imageEntity = userDao.getUserById(id);
            if (imageEntity == null) {
                throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
            }
            else
            return imageEntity;


    }


}
