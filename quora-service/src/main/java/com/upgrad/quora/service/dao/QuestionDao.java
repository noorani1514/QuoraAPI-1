package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
    * Create the question and persist it in the database
    *
    * @param questionEntity the question which needs to be persisted
    * @return question which got created
    * */
    public QuestionEntity createQuestion(QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /**
     * Fetch all the question posted any user
     *
     * @return list of all the questions
    * */
    public List<QuestionEntity> getAllQuestions(){
        try {
            return entityManager.createNamedQuery("getAllQuestions",QuestionEntity.class).getResultList();
        }
        catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * fetch all the questions of a particular user
     *
     * @param userId user id of the user whose questions need to be fetched
     * @return list of all the questions posted by a particular user
     * */
    public List<QuestionEntity> getAllQuestionByUser(String userId){
        try {
            return entityManager.createNamedQuery("getAllQuestionsByUser",QuestionEntity.class).setParameter("userid",userId).getResultList();
        }
        catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * fetch the question with its id
     *
     * @param uuid id of the question
     * @return question details
     * */
    public QuestionEntity getQuestionById(String uuid){
        try {
            return entityManager.createNamedQuery("getQuestionById",QuestionEntity.class).setParameter("uuid",uuid).getSingleResult();
        }
        catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Edit the question content
     * @param editQuestionEntity question which needs to be edited
     * @return question that got edited.
     * */
    public QuestionEntity editQuestionContent(QuestionEntity editQuestionEntity){
        entityManager.merge(editQuestionEntity);
        return editQuestionEntity;
    }

    /**
     * Delete the question
     *
     * @param deleteQuestion question that needs to be deleted
     * */
    public void deleteQuestion(QuestionEntity deleteQuestion){
        entityManager.remove(deleteQuestion);
    }

}
