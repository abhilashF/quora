package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionService {

    @Autowired
    private QuestionDao questionDao;
    public QuestionEntity create(QuestionEntity questionEntity, final String authorization)throws AuthenticationFailedException{

        UserAuthTokenEntity userAuthToken = UserDao.getAuthtoken(authorization);

        if (userAuthToken == null) {
            throw new AuthenticationFailedException("ATHR-001", "User has not Signed in");
        }QuestionEntity.setUser_id(userAuthToken.getUser_id());


        return questionDao.createQuestion(questionEntity);
    }
}
