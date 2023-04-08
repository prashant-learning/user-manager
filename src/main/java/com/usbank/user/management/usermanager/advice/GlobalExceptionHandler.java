package com.usbank.user.management.usermanager.advice;

import com.usbank.user.management.usermanager.exception.RoleNotFoundException;
import com.usbank.user.management.usermanager.exception.UserAlreadyExistException;
import com.usbank.user.management.usermanager.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException exception){

        ErrorResponse errorResponse  = new ErrorResponse(LocalDateTime.now(), "Already exist" , HttpStatus.CONFLICT, exception.getMessage() );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler({RoleNotFoundException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(RoleNotFoundException exception){

        ErrorResponse errorResponse  = new ErrorResponse(LocalDateTime.now(), "Role not found" , HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage() );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
