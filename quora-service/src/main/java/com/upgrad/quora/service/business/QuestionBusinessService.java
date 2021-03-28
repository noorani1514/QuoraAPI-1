package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionBusinessService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(String authorization, String content) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);
        QuestionEntity questionEntity = new QuestionEntity();
        if (userAuthEntity != null) {
            final LocalDateTime now = LocalDateTime.now(); //--? Local or ZonedDateTime?
            questionEntity.setContent(content);
            questionEntity.setDate(now);
            questionEntity.setUser(userAuthEntity.getUser()); //<--- had to set this to set the user
            questionEntity.setUuid(UUID.randomUUID().toString());
            questionDao.createQuestion(questionEntity);
        }

        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions(String authorization) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);

        List<QuestionEntity> getAllQuestions = new ArrayList<>();
        if (userAuthEntity != null) {
            getAllQuestions = questionDao.getAllQuestions();
            return getAllQuestions;
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestionContent(String questionId, String authorization, String content) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);
        QuestionEntity questionEntity = new QuestionEntity();

        if (userAuthEntity != null) {
            questionEntity = questionDao.getQuestionById(questionId);
            if (questionEntity == null) {
                throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
            } else if (!userAuthEntity.getUser().getUuid().equals(questionEntity.getUser().getUuid())) {
                throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
            }
            questionEntity.setContent(content);
            questionEntity = questionDao.editQuestionContent(questionEntity);
        }
        return questionEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(String questionId, String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);
        QuestionEntity questionEntity = new QuestionEntity();

        if (userAuthEntity != null) {
            questionEntity = questionDao.getQuestionById(questionId);
            if (questionEntity == null) {
                throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
            } else if (!userAuthEntity.getUser().getUuid().equals(questionEntity.getUser().getUuid())) {
                throw new AuthorizationFailedException("ATHR-003", "Only the question owner can delete the question");
            }
            questionDao.deleteQuestion(questionEntity);
        }
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestionsByUser(String userId, String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);
        List<QuestionEntity> getAllQuestionsByUser = new ArrayList<QuestionEntity>();
        if (userAuthEntity != null) {
            UserEntity questionUser = userDao.getUserByUUID(userId);
            if (questionUser == null) {
                throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
            } else {
                getAllQuestionsByUser = questionDao.getAllQuestionByUser(userId);
            }
        }
        return getAllQuestionsByUser;
    }
}
