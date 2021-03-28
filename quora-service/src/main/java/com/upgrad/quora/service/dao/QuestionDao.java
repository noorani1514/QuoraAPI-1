package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
        return entityManager.createNamedQuery("getAllQuestions",QuestionEntity.class).getResultList();
    }

    public List<QuestionEntity> getAllQuestionByUser(String userId){
        return entityManager.createNamedQuery("getAllQuestionsByUser",QuestionEntity.class).setParameter("userid",userId).getResultList();
    }

    public QuestionEntity getQuestionById(String uuid){
        return entityManager.createNamedQuery("getQuestionById",QuestionEntity.class).setParameter("uuid",uuid).getSingleResult();
    }

    public QuestionEntity editQuestionContent(QuestionEntity editQuestionEntity){
        entityManager.merge(editQuestionEntity);
        return editQuestionEntity; //<-- instead of returning directly from the line above, doing it after merge operation.
    }

    public void deleteQuestion(QuestionEntity deleteQuestion){
        entityManager.remove(deleteQuestion);
    }

}
