package com.epam.learn.service.rest_app.exception;

import com.epam.learn.dao.jdbc.exeption.ConstraintException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(){
        List<String> errors = List.of("Blog with this name already exists");

        LOGGER.warn("handleIllegalArgument() {}",errors);

        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        LOGGER.warn("handleIllegalArgument() {}",errors);
        return new ResponseEntity<>(new ErrorResponse(errors), headers, status);

    }


    @ExceptionHandler(ConstraintException.class)
    public ResponseEntity<Object> handleConstraint(){
        List<String> errors = List.of("Can't delete Blog because it has dependencies");
        LOGGER.warn("handleIllegalArgument() {}",errors);
        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.FORBIDDEN);
    }

}
