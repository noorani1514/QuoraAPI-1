package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates an answer in the DB.
     *
     * @param answerEntity represents a row of information which is to be persisted.
     * @return persisted answer entity.
     */
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /**
     * Fetches an answer from DB based on the answerId
     *
     * @param answerId id of the answer to be fetched.
     * @return Answer if there exist one with that id in DB else null.
     */
    public AnswerEntity getAnswerById(String answerId) {
        try {
            return entityManager.createNamedQuery("getAnswerById", AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }


    /**
     * Edits the content in answer table of DB.
     *
     * @param answerEntity answer to be updated.
     */
    public AnswerEntity editAnswerContent(AnswerEntity answerEntity) {
        return entityManager.merge(answerEntity);
    }


    /**
     * Delete a answer by given answerId from the DB.
     *
     * @param answerId Id of the answer whose information is to be fetched.
     * @return Answer details which is to be deleted if exist in the DB else null.
     */
    public AnswerEntity deleteAnswer(final String answerId) {
        AnswerEntity deleteAnswer = getAnswerById(answerId);
        if (deleteAnswer != null) {
            entityManager.remove(deleteAnswer);
        }
        return deleteAnswer;
    }


    /**
     * Fetches answer by given questionId from the DB.
     */
    public List<AnswerEntity> getAnswersToQuestion(final String question) {
        try {
            List<AnswerEntity> allAnswersToQuestion = entityManager.createNamedQuery("getAnswerByQuestionId", AnswerEntity.class)
                    .setParameter("questionId", question).getResultList();
            return allAnswersToQuestion;
        } catch (NoResultException nre) {
            return null;
        }
    }

}