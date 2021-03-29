package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerBusinessService {

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionDao questionDao;

    /**
     * Service method to create an answer
     *
     * the signed in user can add the answer with the details as
     * uuid which is generated randomly
     * date as current date time without time zone
     * question to which the answer is been associated
     * user who has given the answer
     *
     * @param answerEntity the answer details that need to added
     * @param authorization authorization of the signed in user from header
     * @param questionId question to which the answer is associated
     *
     * @exception AuthorizationFailedException
     * @exception InvalidQuestionException
     *
     * @return answer which got persisted
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(AnswerEntity answerEntity, final String authorization, final String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(LocalDateTime.now());
        answerEntity.setQuestion(questionEntity);
        answerEntity.setUser(userAuthEntity.getUser());
        return answerDao.createAnswer(answerEntity);
    }

    /**
     * Service method to edit an answer
     *
     * signed in user will try to fetch the answer from the id given
     * then it update his answer which is provided in the parameter
     *
     * @param answerId the answer that need to edited
     * @param authorization authorization of the signed in user from header
     * @param newAnswer answer which replaces the old answer
     *
     * @exception AuthorizationFailedException
     * @exception AnswerNotFoundException
     *
     * @return answer which got edited
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswerContent(
            final String authorization, final String answerId, final String newAnswer)
            throws AnswerNotFoundException, AuthorizationFailedException {
        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);
        AnswerEntity answerEntity = answerDao.getAnswerById(answerId);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        if (!answerEntity.getUser().getUuid().equals(userAuthEntity.getUser().getUuid())) {
            throw new AuthorizationFailedException(
                    "ATHR-003", "Only the answer owner can edit the answer");
        }
        answerEntity.setAns(newAnswer);
        answerDao.editAnswerContent(answerEntity);
        return answerEntity;
    }

    /**
     * Service method to delete an answer
     *
     * signed in user will try to fetch the answer from the id given
     * then he can delete only his answer
     * as well as the admin can delete anyone's answer
     *
     * @param answerId the answer that need to deleted
     * @param authorization authorization of the signed in user from header
     *
     * @exception AuthorizationFailedException
     * @exception AnswerNotFoundException
     *
     * @return answer which got deleted
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(final String answerId, final String authorization)
            throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);
        AnswerEntity answerEntity = answerDao.getAnswerById(answerId);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        else if(!userAuthEntity.getUser().getRole().equalsIgnoreCase("admin")) {
            if(!answerEntity.getUser().getUuid().equals(userAuthEntity.getUser().getUuid())) {
                throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
            }
        }
        return answerDao.deleteAnswer(answerId);
    }

    /**
     * Service method to get all answers to a particular question
     *
     * signed in user can fetch all the answers to a particular question
     *
     * @param questionId the question id whose answer need to fetched
     * @param authorization authorization of the signed in user from header
     *
     * @exception AuthorizationFailedException
     * @exception AnswerNotFoundException
     * @exception InvalidQuestionException
     *
     * @return list of all the answers of that particuar question
     * */
    public List<AnswerEntity> getAllAnswersToQuestion(
            final String authorization, final String questionId)
            throws AuthorizationFailedException, InvalidQuestionException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = authenticationService.checkAuthentication(authorization);
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException(
                    "QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }
        List<AnswerEntity> answersList = answerDao.getAnswersToQuestion(questionId);
        if(answersList.isEmpty()) {
            throw new AnswerNotFoundException("ANS-002", "No Answer exists for this question");
        }
        return answersList;
    }
}