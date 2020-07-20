package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
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
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;
    //This controller method is called when the request pattern is of type '/question/{questionId}/answer/create'
    @RequestMapping(value = "/question/{questionId}/answer/create", method = RequestMethod.POST)
    public ResponseEntity<AnswerResponse> createAnswer(final UserEntity userEntity, @RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String id, AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {




        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerRequest.getAnswer());
        answerService.create(answerEntity);



        UserAuthTokenEntity userAuthTokenEntity = userService.getAuthtoken(authorization);

        if (!id.equals(userEntity.getUuid())){
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }


        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if (userService.getUserById((id))!=userEntity){
            throw new AuthorizationFailedException ("ATHR-002","User is signed out.Sign in first to post an answer");
        }

        else {


            answerEntity.setAns(answerRequest.getAnswer());
            answerEntity.setUuid(UUID.randomUUID().toString());


             AnswerEntity createdAnswer = answerService.create(answerEntity);
            AnswerResponse answerResponse = new AnswerResponse().id(createdAnswer.getUuid()).status("ANSWER CREATED'");
            return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
        }


    }

    //This controller method is called when the request pattern is of type '/answer/edit/{answerId}'
    @RequestMapping(value = "/answer/edit/{answerId}", method = RequestMethod.PUT)
    public ResponseEntity <AnswerEditRequest> editAnswerContent(@RequestHeader("authorization") final String authorization, UserEntity userEntity, @PathVariable("questionId") final String questionId, AnswerEditRequest answerEditRequest) throws AuthorizationFailedException, AnswerNotFoundException {

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerEditRequest.getContent());
        answerService.editAnswerContent(answerEntity);


        UserAuthTokenEntity userAuthTokenEntity = userService.getAuthtoken(authorization);


        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userService.getUserById((questionId))!=userEntity){
            throw new AuthorizationFailedException ("ATHR-002","User is signed out.Sign in first to edit an answer");
        }
        if(!answerEntity.getUuid().equals(userEntity.getUuid())){

            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }
        if (answerEntity.getUuid()!=userEntity.getUuid()){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist" );
        }
        else {
             AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER EDITED");


            return new ResponseEntity<AnswerEditRequest>(answerEditRequest, (MultiValueMap<String, String>) answerResponse, HttpStatus.OK);

        }

    }
    //This controller method is called when the request pattern is of type '/answer/delete/{answerId}'
    @RequestMapping(value = "/answer/delete/{answerId}", method = RequestMethod.DELETE)
    public ResponseEntity<AnswerResponse> deleteAnswer(@RequestHeader("authorization") final String authorization, UserEntity userEntity, @PathVariable("answerId") final String id, Integer userid, Model model) throws AuthorizationFailedException, InvalidQuestionException, AnswerNotFoundException {

        AnswerEntity answerEntity = new AnswerEntity();

        AnswerEntity answer = answerService.deleteAnswer(userid);
        UserAuthTokenEntity userAuthTokenEntity = userService.getAuthtoken(authorization);

        model.addAttribute("answer", answer);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userService.getUserById((id)) != userEntity) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete an answer");
        }
        if (!answerEntity.getUuid().equals(userEntity.getUuid()) ){

            throw new InvalidQuestionException("ATHR-003", "Only the question owner or admin can delete the answer");

        }
        if (answerEntity.getUuid()!=userEntity.getUuid()){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist" );
        }

        else {
            answerService.deleteAnswer(userid);
             AnswerEntity deletedAnswer = answerService.deleteAnswer(userid);

            AnswerResponse answerResponse = new AnswerResponse().id(deletedAnswer.getUuid()).status("Answer DELETED'");
            return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.NOT_FOUND);


        }
    }
    //This controller method is called when the request pattern is of type 'answer/all/{questionId}'
    @RequestMapping(value = "answer/all/{questionId}", method = RequestMethod.GET)
    public ResponseEntity<AnswerResponse> getAllAnswersToQuestion(@RequestHeader("authorization") final String authorization, UserEntity userEntity, @PathVariable("questionId") final Integer id,String uuid, Model model) throws AuthorizationFailedException, InvalidQuestionException {

        AnswerEntity answerEntity = new AnswerEntity();
        List<AnswerEntity> answer = answerService.getAllAnswersByUser(id);
        UserAuthTokenEntity userAuthTokenEntity = userService.getAuthtoken(authorization);

        model.addAttribute("answer", answer);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userService.getUserById((uuid))!=userEntity) {
            throw new AuthorizationFailedException("ATHR-002", "'User is signed out.Sign in first to get all questions posted by a specific user");
        }
        if (userService.getUserById(uuid) != null) {

            throw new InvalidQuestionException("Ques-001", "Entered question uuid does not exist");

        } else {
            AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid());

            return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.OK);

        }
    }

}
