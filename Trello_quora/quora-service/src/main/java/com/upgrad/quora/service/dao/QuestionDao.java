package com.upgrad.quora.service.dao;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
import java.util.List;

//The annotation is a special type of @Component annotation which describes that the class defines a data repository
@Repository
public class QuestionDao {
    //Get an instance of EntityManagerFactory from persistence unit with name as 'quora'
    @PersistenceUnit(unitName = "quora")
    private EntityManagerFactory emf;

    //The method receives the question object to be persisted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
        entityManager.persist(questionEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return questionEntity;

    }

    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch all the questions from the database
    //Returns the list of all the questions fetched from the database

    public List<QuestionEntity> getAllQuestions() {
          EntityManager entityManager = emf.createEntityManager();

        try {

        TypedQuery<QuestionEntity> query = entityManager.createQuery("SELECT i from QuestionEntity i", QuestionEntity.class);
        List<QuestionEntity> resultList = query.getResultList();
        return resultList;
        } catch (NoResultException nre) {
            return null;
        }
    }
    //Creates an instance of EntityManager
    //Starts a transaction
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction



    public void editQuestionContent(QuestionEntity editedQuestion){
          EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(editedQuestion);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }

    }
    //The method receives the User id of the answer to be deleted in the database
    //Creates an instance of EntityManager
    //Starts a transaction
    //Get the question with corresponding user id from the database
    //This changes the state of the question model from detached state to persistent state, which is very essential to use the remove() method
    //If you use remove() method on the object which is not in persistent state, an exception is thrown
    //The transaction is committed if it is successful
    //The transaction is rolled back in case of unsuccessful transaction

    public void deleteQuestion(Integer user_id) {
          EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            QuestionEntity question = entityManager.find(QuestionEntity.class, user_id);
            entityManager.remove(question);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    //The method creates an instance of EntityManager
    //Executes JPQL query to fetch the questions from the database with corresponding userId
    //Returns the answer in case the questons is found in the database
    //Returns null if no answer is found in the database
    public List<QuestionEntity>getAllQuestionByUser (Integer userId) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            TypedQuery<QuestionEntity> typedQuery = entityManager.createQuery("SELECT i from QuestionEntity i where i.id =:id", QuestionEntity.class).setParameter("id", userId);
            List<QuestionEntity> resultList = typedQuery.getResultList();

            return resultList;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
