package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.stereotype.Controller;

@Controller
public class CommonController {
    public UserEntity userProfile(final String Uuid, final String authorization) throws UserNotFoundException, AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = UserDao.getAuthtoken(authorization);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        String role = userAuthTokenEntity.getUser_id().getRole();

            UserEntity imageEntity = UserDao.getUser(Uuid);
            if (imageEntity == null) {
                throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
            }
            else
            return imageEntity;


    }


}
