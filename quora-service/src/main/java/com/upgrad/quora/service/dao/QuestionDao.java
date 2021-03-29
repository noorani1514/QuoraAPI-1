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

    public QuestionEntity createQuestion(QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions(){
        try {
            return entityManager.createNamedQuery("getAllQuestions",QuestionEntity.class).getResultList();
        }
        catch (NoResultException nre) {
            return null;
        }
    }

    public List<QuestionEntity> getAllQuestionByUser(String userId){
        try {
            return entityManager.createNamedQuery("getAllQuestionsByUser",QuestionEntity.class).setParameter("userid",userId).getResultList();
        }
        catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity getQuestionById(String uuid){
        try {
            return entityManager.createNamedQuery("getQuestionById",QuestionEntity.class).setParameter("uuid",uuid).getSingleResult();
        }
        catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity editQuestionContent(QuestionEntity editQuestionEntity){
        entityManager.merge(editQuestionEntity);
        return editQuestionEntity; //<-- instead of returning directly from the line above, doing it after merge operation.
    }

    public void deleteQuestion(QuestionEntity deleteQuestion){
        entityManager.remove(deleteQuestion);
    }

}
