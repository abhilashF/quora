package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    //This controller method is called when the request pattern is of type '/question/create' and also the incoming request is of POST type
    //The method receives all the details of the question to be stored in the database, and now the question will be sent to the business logic to be persisted in the database

    @RequestMapping(value = "/question/create", method = RequestMethod.POST)
    public ResponseEntity<QuestionResponse> createQuestion(final UserEntity userEntity, @RequestHeader("authorization") final String authorization, @PathVariable("userId") final String id, QuestionRequest questionRequest ) throws AuthorizationFailedException {




        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());


        UserAuthTokenEntity userAuthTokenEntity = userService.getAuthtoken(authorization);


        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userService.getUserById((id))!=userEntity){
            throw new AuthorizationFailedException ("ATHR-002","User is signed out.Sign in first to post a question'");
        }
        else {

            questionEntity.setContent(questionRequest.getContent());
            questionEntity.setUuid(UUID.randomUUID().toString());


            final QuestionEntity createdQuestion = questionService.create(questionEntity);
            QuestionResponse questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED'");
            return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
        }
    }
    //This controller method is called when the request pattern is of type '/question/al' and also the incoming request is of GET type

    @RequestMapping(value = "/question/al", method = RequestMethod.GET)
    public ResponseEntity<QuestionResponse> getAllQuestions(@RequestHeader("authorization") final String authorization, Model model, UserEntity userEntity, @PathVariable("userId") final String id) throws AuthorizationFailedException {

        QuestionEntity questionEntity = new QuestionEntity();

        List<QuestionEntity> questions = questionService.getAllQuestions();
        model.addAttribute("questions", questions);
        UserAuthTokenEntity userAuthTokenEntity = userService.getAuthtoken(authorization);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userService.getUserById((id))!=userEntity){
            throw new AuthorizationFailedException ("ATHR-002","User is signed out.Sign in first to get all questions'");
        }

        else {

            QuestionResponse questionResponse = new QuestionResponse().id(questionEntity.getUuid());
            return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
        }
    }
    //This controller method is called when the request pattern is of type '/question/edit/{questionId} and also the incoming request is of PUT type

    @RequestMapping(value = "/question/edit/{questionId}", method = RequestMethod.PUT)
    public ResponseEntity <QuestionEditRequest> editQuestionContent(@RequestHeader("authorization") final String authorization,UserEntity userEntity, @PathVariable("questionId") final String questionId,QuestionEditRequest questionEditRequest) throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionEditRequest.getContent());
        questionService.questionContent(questionEntity);


        UserAuthTokenEntity userAuthTokenEntity = userService.getAuthtoken(authorization);


        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userService.getUserById((questionId))!=userEntity){
            throw new AuthorizationFailedException ("ATHR-002","User is signed out.Sign in first to edit the question");
        }
        if(!questionEntity.getUuid().equals(userEntity.getUuid())){

            throw new InvalidQuestionException("ATHR-003","Only the question owner can edit the question");
        }
        else {
            QuestionResponse questionResponse = new QuestionResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");


            return new ResponseEntity<QuestionEditRequest>(questionEditRequest, (MultiValueMap<String, String>) questionResponse, HttpStatus.OK);

        }

    }
    //This controller method is called when the request pattern is of type '/question/delete/{questionId}' and also the incoming request is of DELETE type

    @RequestMapping(value = "/question/delete/{questionId}", method = RequestMethod.DELETE)
    public ResponseEntity<QuestionResponse> deleteQuestion(@RequestHeader("authorization") final String authorization, UserEntity userEntity, @PathVariable("questionId") final String id, Integer userid, Model model) throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity = new QuestionEntity();

       QuestionEntity question = questionService.deleteQuestion(userid);
        UserAuthTokenEntity userAuthTokenEntity = userService.getAuthtoken(authorization);

        model.addAttribute("question", question);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userService.getUserById((id)) != userEntity) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }
        if (!questionEntity.getUuid().equals(userEntity.getUuid()) ){

            throw new InvalidQuestionException("ATHR-003", "Only the question owner or admin can delete the question");

        } else {
            questionService.deleteQuestion(userid);
            final QuestionEntity deletedQuestion = questionService.deleteQuestion(userid);

            QuestionResponse questionResponse = new QuestionResponse().id(deletedQuestion.getUuid()).status("QUESTION DELETED'");
            return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.NOT_FOUND);


        }
    }
    //This controller method is called when the request pattern is of type 'question/all/{userId}' and also the incoming request is of GET type

    @RequestMapping(value = "question/all/{userId}", method = RequestMethod.GET)
    public ResponseEntity<QuestionResponse> getAllQuestionsByUser(@RequestHeader("authorization") final String authorization, UserEntity userEntity, @PathVariable("user_id") final Integer id, String uuid, Model model) throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity = new QuestionEntity();

        List<QuestionEntity> question = questionService.getAllQuestionByUser(id);
        UserAuthTokenEntity userAuthTokenEntity = userService.getAuthtoken(authorization);


        model.addAttribute("question", question);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userService.getUserById((uuid))!=userEntity) {
            throw new AuthorizationFailedException("ATHR-002", "'User is signed out.Sign in first to get all questions posted by a specific user");
        }
        if (userService.getUserById(uuid) != null) {

            throw new InvalidQuestionException("Ques-001", "Entered question uuid does not exist");

        } else {
            QuestionResponse questionResponse = new QuestionResponse().id(questionEntity.getUuid());

            return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
        }
    }

  }
