package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class QuestionService {



    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    //The method calls the create() method in the Repository and passes the question to be created in the database

    public QuestionEntity create(QuestionEntity questionEntity){

        return questionDao.createQuestion(questionEntity);
    }

    //The method calls the questionContent() method in the Repository and passes the Content of the question to be updated in the database

    public QuestionEntity questionContent (QuestionEntity editedquestion){
        questionDao.editQuestionContent(editedquestion);
        return editedquestion;
    }
    //Call the getAllQuestions() method in the Repository and obtain a List of all the questions in the database

    public  List<QuestionEntity>getAllQuestions() {
        return questionDao.getAllQuestions();
    }

    //The method calls the deleteQuestion() method in the Repository and passes the user id of the Question to be deleted in the database

    public QuestionEntity deleteQuestion(Integer user_id) {
         questionDao.deleteQuestion(user_id);

        return null;
    }
    //The method calls the getQuestionByUser() method in the Repository and passes the user id of the question to be fetched
    public List<QuestionEntity>getAllQuestionByUser (Integer user_id) {
        return questionDao.getAllQuestionByUser(user_id);
    }
}
