package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerBusinessService answerBusinessService;

    /**
     * Controller method to create a answer
     *
     * calls the create answer method of the answer service
     *
     * @param accessToken authorization of the signed in user from header
     * @param questionId question id of the question whose answer needs to be added
     *
     * @exception AuthorizationFailedException
     * @exception InvalidQuestionException
     *
     * @return  the 201 status code with the id of the answer with status ANSWER CREATED
     * */
    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("questionId") final String questionId,
            AnswerRequest answerRequest)
            throws AuthorizationFailedException, InvalidQuestionException {
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerRequest.getAnswer());
        answerEntity = answerBusinessService.createAnswer(answerEntity, accessToken, questionId);
        AnswerResponse answerResponse = new AnswerResponse();
        answerResponse.setId(answerEntity.getUuid());
        answerResponse.setStatus("ANSWER CREATED");
        return new ResponseEntity<>(answerResponse, HttpStatus.CREATED);
    }

    /**
     * Controller method to edit an answer
     *
     * calls the edit answer method of the answer service
     *
     * @param accessToken authorization of the signed in user from header
     * @param answerId id of the answer which needs to be edited
     *
     * @exception AuthorizationFailedException
     * @exception AnswerNotFoundException
     *
     * @return  the 200 status code with the id of the answer with status ANSWER EDITED
     * */
    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("answerId") final String answerId,
            AnswerEditRequest answerEditRequest)
            throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEditResponse answerEditResponse = new AnswerEditResponse();

        AnswerEntity answerEntity = answerBusinessService.editAnswerContent(accessToken, answerId, answerEditRequest.getContent());
        answerEditResponse.setId(answerEntity.getUuid());
        answerEditResponse.setStatus("ANSWER EDITED");
        return new ResponseEntity<>(answerEditResponse, HttpStatus.OK);
    }

    /**
     * Controller method to delete an answer
     *
     * calls the delete answer method of the answer service
     *
     * @param accessToken authorization of the signed in user from header
     * @param answerId id of the answer which needs to be edited
     *
     * @exception AuthorizationFailedException
     * @exception AnswerNotFoundException
     *
     * @return  the 200 status code with the id of the answer with status ANSWER DELETED
     * */
    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/answer/delete/{answerId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("answerId") String answerId)
            throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = answerBusinessService.deleteAnswer(answerId, accessToken);
        AnswerDeleteResponse answerDeleteResponse =
                new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<>(answerDeleteResponse, HttpStatus.OK);
    }

    /**
     * Controller method to get all answers
     *
     * calls the get all answers method of the answer service
     *
     * @param authorization authorization of the signed in user from header
     * @param questionId id of the answer which needs to be edited
     *
     * @exception AuthorizationFailedException
     * @exception AnswerNotFoundException
     *
     * @return  the 200 status code with all the answers id and the answers
     * */
    @RequestMapping(method = RequestMethod.GET, path = "/answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(
            @RequestHeader("authorization") final String authorization,
            @PathVariable("questionId") final String questionId)
            throws AuthorizationFailedException, InvalidQuestionException, AnswerNotFoundException {
        List<AnswerDetailsResponse> answerDetailsResponse = new ArrayList<AnswerDetailsResponse>();
        List<AnswerEntity> answerEntityList = answerBusinessService
                .getAllAnswersToQuestion(authorization, questionId);
        for (AnswerEntity answerEntity : answerEntityList) {
            answerDetailsResponse.add(new AnswerDetailsResponse().id(answerEntity.getUuid())
                    .answerContent(answerEntity.getAns())
                    .questionContent(answerEntity.getQuestion().getContent()));
        }
        return new ResponseEntity<>(answerDetailsResponse, HttpStatus.OK);
    }
}