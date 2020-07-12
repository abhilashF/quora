package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class AdminController {
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity userDelete(final UserEntity userEntity,String userId, final String authorization) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = UserDao.getAuthtoken(authorization);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (UserDao.getUser(userId)!=userEntity){
            throw new AuthorizationFailedException ("ATHR-002","User is signed out");
        }

        String role = userAuthTokenEntity.getUser_id().getRole();
        if (role !=("admin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");

            UserEntity existinguserEntity = UserDao.getUserById(userEntity.getId());

            if (existinguserEntity == null) {
                throw new AuthorizationFailedException("USR-001", "User with entered uuid to be deleted does not exist");
            }




        } else {
            return UserDao.getUser(userId);

        }
    }
}
