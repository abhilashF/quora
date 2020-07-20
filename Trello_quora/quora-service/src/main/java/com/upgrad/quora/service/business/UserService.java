package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Service
public class UserService {



    @Autowired
    private UserDao userDao;

    public UserEntity signup(UserEntity userEntity) {
        userDao.signup(userEntity);
        return userEntity;
    }

    public UserEntity signin(UserEntity userEntity) {
        UserEntity existingUser = userDao.checkUser(userEntity.getUsername(), userEntity.getPassword());
        if (existingUser != null) {
            return existingUser;
        } else {
            return null;
        }
    }
    public UserEntity signout(UserEntity userEntity) {
        UserEntity existingUser = userDao.checkUser(userEntity.getUsername(), userEntity.getPassword());
        if (existingUser != null) {
            return existingUser;
        } else {
            return null;
        }
    }
    public UserEntity getUserByUsername(String username) {
       return userDao.getUserByUsername(username);
    }
    public UserAuthTokenEntity getAuthtoken(final String accessToken) {
        return userDao.getAuthtoken(accessToken);
    }
    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {

        return userAuthTokenEntity;
    }
    public UserEntity getUserById(final String uuid) {
        return userDao.getUserById(uuid);
    }
    public void updateUser(UserEntity userEntity) {
        userDao.updateUser(userEntity);
    }
}
