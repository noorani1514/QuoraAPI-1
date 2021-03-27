package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signupRestrictedException(SignUpRestrictedException sre, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
            new ErrorResponse().code(sre.getCode()).message(sre.getErrorMessage()), HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException afe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(afe.getCode()).message(afe.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException sre, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(sre.getCode()).message(sre.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException afe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(afe.getCode()).message(afe.getErrorMessage()), HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(UserNotFoundException une, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(une.getCode()).message(une.getErrorMessage()), HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    @ExceptionHandler(InvalidQuestionException.class) //QuestionException
    public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException iqe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(iqe.getCode()).message(iqe.getErrorMessage()), HttpStatus.UNPROCESSABLE_ENTITY
        );
    }
}
