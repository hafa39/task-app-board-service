package com.example.boardservice.web.api;

import com.example.boardservice.domain.exc.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExcHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExcHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        //ITERATE THROUGH ERRORS
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        for (ObjectError objectError : objectErrors) {
            //GET ERROR
            FieldError fieldError = (FieldError) objectError;
            String fieldName =fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            //STORE ERROR
            errors.put(fieldName, errorMessage);
        }
        log.error("Validation Failure: {}", errors );
        return errors;
    }
    @ExceptionHandler(OptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,String> handleOpLockExc(OptimisticLockingFailureException e){
        var error = Map.of("message","Transaction Failure");
        log.error("Transaction Failure {}",e.toString());
        return error;
    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundExc(NotFoundException e) {
        log.error("NotFoundException: " + e.getMessage());
        return Map.of("message", e.getMessage());
    }
}
