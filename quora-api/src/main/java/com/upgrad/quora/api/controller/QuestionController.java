package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")

public class QuestionController {

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(
            @RequestHeader("authorization") final String authorization, final QuestionRequest questionRequest) throws AuthorizationFailedException {
        QuestionResponse questionResponse = new QuestionResponse();
        QuestionEntity questionEntity = questionBusinessService.createQuestion(authorization, questionRequest.getContent());
        questionResponse.setId(questionEntity.getUuid());
        questionResponse.setStatus("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();
        List<QuestionEntity> questionEntityList = new ArrayList<>();

        questionEntityList = questionBusinessService.getAllQuestions(authorization);

        for (QuestionEntity questionEntity : questionEntityList) {
            QuestionDetailsResponse questionDetailsResponse_obj = new QuestionDetailsResponse();
            questionDetailsResponse_obj.setId(questionEntity.getUuid());
            questionDetailsResponse_obj.setContent(questionEntity.getContent());
            questionDetailsResponseList.add(questionDetailsResponse_obj);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId, final QuestionEditRequest questionEditRequest) throws AuthorizationFailedException, InvalidQuestionException{
        QuestionEditResponse questionEditResponse_obj = new QuestionEditResponse();
        QuestionEntity questionEntity = questionBusinessService.editQuestionContent(questionId,authorization,questionEditRequest.getContent());
        questionEditResponse_obj.setId(questionEntity.getUuid());
        questionEditResponse_obj.setStatus("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse_obj,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId) throws AuthorizationFailedException, InvalidQuestionException{
        QuestionDeleteResponse questionDeleteResponse_obj = new QuestionDeleteResponse();
        QuestionEntity questionEntity = questionBusinessService.deleteQuestion(questionId, authorization);
        questionDeleteResponse_obj.setId(questionEntity.getUuid());
        questionDeleteResponse_obj.setStatus("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse_obj,HttpStatus.OK);
        }

    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("authorization") final String authorization, @PathVariable("userId") final String userId) throws AuthorizationFailedException, UserNotFoundException{
        List <QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();
        List <QuestionEntity> questionEntityList = new ArrayList<>();
        questionEntityList = questionBusinessService.getAllQuestionsByUser(userId,authorization);

        for (QuestionEntity questionEntity : questionEntityList){
            QuestionDetailsResponse questionDetailsResponse_obj = new QuestionDetailsResponse();

            questionDetailsResponse_obj.setId(questionEntity.getUuid());
            questionDetailsResponse_obj.setContent(questionEntity.getContent());
            questionDetailsResponseList.add(questionDetailsResponse_obj);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList,HttpStatus.OK);
    }

}






