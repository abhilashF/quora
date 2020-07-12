package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity create(QuestionEntity questionEntity, final String authorization)throws AuthenticationFailedException{

        UserAuthTokenEntity userAuthToken = questionDao.getAuthToken(authorization);

        if (userAuthToken == null) {
            throw new AuthenticationFailedException("ATHR-001", "User has not Signed in");
        }

            questionEntity.setUser_id(userAuthToken.getUser_id());


        return questionDao.createQuestion(questionEntity);
    }
}
