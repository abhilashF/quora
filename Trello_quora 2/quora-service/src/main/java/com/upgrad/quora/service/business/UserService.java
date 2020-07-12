package com.upgrad.quora.service.business;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    public static void deleteUser(Integer imageId) {
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) {
        String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);

        return userDao.createUser(userEntity);
    }
    UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorization);
    if (userAuthTokenEntity == null) {
        throw new UserNotSignedInException("USR-001", "You are not Signed in, sign in first to get the details of the image");
    }
}
