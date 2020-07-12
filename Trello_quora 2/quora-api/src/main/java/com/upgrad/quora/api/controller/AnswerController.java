package com.upgrad.quora.api.controller;

import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import java.io.UnsupportedEncodingException;

@Controller
public class AnswerController {
    @RequestMapping(method = RequestMethod.POST, path = "/Answerupload", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerUploadResponse> createAnswer(final AnswerUploadRequest AnswerUploadRequest, @RequestHeader("authorization") final String authorization) throws UploadFailedException, UnsupportedEncodingException {


        final AnswerEntity createdimageEntity = AnswerUploadService.upload(AnswerEntity, authorization);
        AnswerUploadResponse AnswerUploadResponse = new AnswerUploadResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerUploadResponse>(AnswerUploadResponse, HttpStatus.CREATED);
    }
    @RequestMapping(method = RequestMethod.PUT, path = "/Answer/update/{userId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateAnswerResponse> editAnswerContent(final UpdateAnswerRequest updateAnswerRequest, @PathVariable("userId") final long userId, @RequestHeader("authorization") final String authorization) throws ImageNotFoundException, UnauthorizedException, UserNotSignedInException {
        AnswerEntity AnswerEntity = new AnswerEntity();

        AnswerEntity.setAnswer(updateAnswerRequest.getAnswer());
        AnswerEntity.setId(userId);
        AnswerEntity.setName(updateAnswerRequest.getName());
        AnswerEntity.setStatus(updateAnswerRequest.getStatus());
        AnswerEntity.setDescription(updateAnswerRequest.getDescription());


       AnswerEntity updatedAnswerEntity = UserService.updateAnswer(AnswerEntity, authorization);
        UpdateAnswerResponse updateAnswerResponse = new UpdateAnswerResponse().id((int) updatedAnswerEntity.getId()).status(updatedAnswerEntity.getStatus());

        return new ResponseEntity<UpdateAnswerResponse>(updateAnswerResponse, HttpStatus.OK);
    }
    public String deleteAnswer(@RequestParam(name = "userId") Integer userId) {
        AnswerService.deleteAnswer(userId);
        return "redirect:/answers";
    }
    public UserEntity getAllAnswersToQuestion(final long Id) {
        try {
            return entityManager.createNamedQuery("AnswerEntityByid", AnswerEntity.class).setParameter("id", Id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
