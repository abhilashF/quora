package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import javax.persistence.*;

//The annotation is a special type of @Component annotation which describes that the class defines a data repository
@Repository
public class UserDao {

    //Get an instance of EntityManagerFactory from persistence unit with name as 'quora'

    @PersistenceUnit(unitName = "quora")
    private EntityManagerFactory emf;
    public UserEntity getUserByUsername;



    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }


    public void updateUser(UserEntity userEntity) {
    }

    //The method receives the entered username and password
    //Creates an instance of EntityManager
    //Executes JPQL query to fetch the user from User class where username is equal to received username and password is equal to received password
    //Returns the fetched user
    //Returns null in case of NoResultException
    public UserEntity checkUser(String username, String password) {

        try {
            EntityManager em = emf.createEntityManager();
            TypedQuery<UserEntity> typedQuery = em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username AND u.password = :password", UserEntity.class);
            typedQuery.setParameter("username", username);
            typedQuery.setParameter("password", password);

            return typedQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }
    //The method receives the User object to be persisted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction
    public void signup(UserEntity userEntity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            //persist() method changes the state of the model object from transient state to persistence state
            em.persist(userEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }



    public void deleteUser(Integer user_id) {
        EntityManager entityManager = emf.createEntityManager();


        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            UserEntity User = entityManager.find(UserEntity.class, user_id);
            entityManager.remove(User);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }
    public UserEntity getUserByUsername(String username) {
        try {
            EntityManager em = emf.createEntityManager();
            TypedQuery<UserEntity> typedQuery = em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class);
            typedQuery.setParameter("username", username);
            return typedQuery.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public UserAuthTokenEntity getAuthtoken(final String accessToken) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken",
                    UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserById(final String uuid) {
        EntityManager entityManager = emf.createEntityManager();
        return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", uuid).getSingleResult();

    }
}
