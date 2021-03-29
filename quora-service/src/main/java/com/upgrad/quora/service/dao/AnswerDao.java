package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerById(String answerId) {
        try {
            return entityManager.createNamedQuery("getAnswerById", AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    public AnswerEntity editAnswerContent(AnswerEntity answerEntity) {
        return entityManager.merge(answerEntity);
    }


    /**
     * Delete a answer by given answerId from the DB.
     */

    public AnswerEntity deleteAnswer(final String answerId) {
        AnswerEntity deleteAnswer = getAnswerById(answerId);
        if (deleteAnswer != null) {
            entityManager.remove(deleteAnswer);
        }
        return deleteAnswer;
    }

    public List<AnswerEntity> getAnswersToQuestion(final String question) {
        try {
            List<AnswerEntity> allAnswersToQuestion = entityManager.createNamedQuery("getAnswersByQuestionId", AnswerEntity.class)
                    .setParameter("question", question).getResultList();
            return allAnswersToQuestion;
        } catch (NoResultException nre) {
            return null;
        }
    }

//    public List<AnswerEntity> getAnswerByQuestionId(String questionId) {
//        return entityManager.createNamedQuery("getAnswerByQuestionId", AnswerEntity.class).setParameter("questionId", questionId).getResultList();
//    }
}