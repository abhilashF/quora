package com.upgrad.quora.service.business;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public static void deleteUser(String uuid) {
    }

    public UserEntity signup(UserEntity userEntity) {
        return userEntity;
    }
}
