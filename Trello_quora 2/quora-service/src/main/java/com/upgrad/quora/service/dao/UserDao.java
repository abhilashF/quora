package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {
    @PersistenceContext
    private static EntityManager entityManager;




    public static UserEntity getUser(String imageUuid) {
    }

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }
    public static UserAuthTokenEntity getAuthtoken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("UserAuthTokenByaccessToken",
                    UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    public static UserEntity getUserById(final long id) {
        return entityManager.createNamedQuery("userById", UserEntity.class).setParameter("id", id).getSingleResult();

    }
}
