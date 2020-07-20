package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

//The annotation is a special type of @Component annotation which describes that the class defines a data repository
@Repository
public class AnswerDao {

    //Get an instance of EntityManagerFactory from persistence unit with name as 'quora'

    @PersistenceUnit(unitName = "quora")

    private EntityManagerFactory emf;

    //The method receives the answer object to be persisted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
        entityManager.persist(answerEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return answerEntity;

    }
    public UserAuthTokenEntity getAuthToken(final String accessToken) {
        EntityManager entityManager = emf.createEntityManager();
        try {

            return entityManager.createNamedQuery("userAuthTokenByAccessToken",
                    UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public AnswerEntity getUserById(final Integer user_id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.createNamedQuery("AnswerEntityByid", AnswerEntity.class).setParameter("id", user_id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public AnswerEntity getUserByUuid(final String uuid) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.createNamedQuery("AnswerEntityByUuid", AnswerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction

    public void editAnswerContent(AnswerEntity editedAnswer){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(editedAnswer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

    }
    //The method receives the User id of the answer to be deleted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //Get the answer with corresponding user id from the database
    //This changes the state of the answer model from detached state to persistent state, which is very essential to use the remove() method
    //If you use remove() method on the object which is not in persistent state, an exception is thrown
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction

    public void deleteAnswer(Integer user_id) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            AnswerEntity answer = entityManager.find(AnswerEntity.class, user_id);
            entityManager.remove(answer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }
    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch the answer from the database with corresponding userId
    //Returns the answer in case the answer is found in the database
    //Returns null if no answer is found in the database
    public List<AnswerEntity>getAllAnswersByUser (Integer userId) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<AnswerEntity> typedQuery = entityManager.createQuery("SELECT i from QuestionEntity i where i.id =:id", AnswerEntity.class).setParameter("id", userId);

            return typedQuery.getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
