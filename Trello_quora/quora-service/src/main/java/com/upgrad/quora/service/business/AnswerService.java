package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;

import com.upgrad.quora.service.entity.AnswerEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    //The method calls the create() method in the Repository and passes the answer to be created in the database

    public AnswerEntity create(AnswerEntity answerEntity){

        return answerDao.createAnswer(answerEntity);
    }

    //The method calls the editAnswerContent() method in the Repository and passes the Content of the answer to be updated in the database
    public AnswerEntity editAnswerContent (AnswerEntity editedAnswer){
        answerDao.editAnswerContent(editedAnswer);
        return editedAnswer;
    }


    //The method calls the deleteAnswer() method in the Repository and passes the user id of the Answer to be deleted in the database

    public AnswerEntity deleteAnswer(Integer user_id) {
        answerDao.deleteAnswer(user_id);

        return null;
    }
    //The method calls the getAnswersByUser() method in the Repository and passes the user id of the answer to be fetched

    public List<AnswerEntity>getAllAnswersByUser (Integer user_id) {
        return answerDao.getAllAnswersByUser(user_id);
    }
}
