package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class QuestionController {

    @Autowired
    private QuestionDao questionDao;

    @RequestMapping(value = "/question/create", method = RequestMethod.POST)
    public String createQuestion(@RequestHeader("authorization") final String authorization,  QuestionRequest questionRequest, @RequestParam("question") String question,   HttpSession session) throws AuthenticationFailedException {




        User user = (User) session.getAttribute("loggeduser");
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());





    }

    public String getAllQuestions(Model model) {
        List<String> questions = questionService.getAllQuestions();
        model.addAttribute("Questions", questions);
        return "questions";
    }
    public String editQuestionContent(@RequestParam("userId") Integer userId, Model model) {
        String question = QuestionService.getquestion(userId);

        model.addAttribute("question", question);
        return "questions/edit";
    }

    @RequestMapping(value = "/deleteQuestion", method = RequestMethod.DELETE)
    public String deleteQuestion(@RequestParam(name = "userId") Integer userId) {
        QuestionService.deleteQuestion(userId);
        return "redirect:/questions";
    }
    public UserEntity getAllQuestionsByUser(final long Id) {
        try {
            return entityManager.createNamedQuery("UserEntityByid", UserEntity.class).setParameter("id", Id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
