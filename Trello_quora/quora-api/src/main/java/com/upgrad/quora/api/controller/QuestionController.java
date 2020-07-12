package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/question/create", method = RequestMethod.POST)
    public ResponseEntity<QuestionResponse> createQuestion(final UserEntity userEntity, @RequestHeader("authorization") final String authorization, @PathVariable("userId") final String id, String uuid
            , QuestionRequest questionRequest, @RequestParam("question") String question, HttpSession session) throws AuthorizationFailedException, AuthenticationFailedException {




        User user = (User) session.getAttribute("loggeduser");
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());


        UserAuthTokenEntity userAuthTokenEntity = userDao.getAuthtoken(authorization);


        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userDao.getUserById((id))!=userEntity){
            throw new AuthorizationFailedException ("ATHR-002","User is signed out.Sign in first to post a question'");
        }
        else {

            questionEntity.setContent(questionRequest.getContent());
            questionEntity.setUuid(UUID.randomUUID().toString());


            final QuestionEntity createdQuestion = questionService.create(questionEntity, authorization);
            QuestionResponse questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED'");
            return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
        }



    }

  }
