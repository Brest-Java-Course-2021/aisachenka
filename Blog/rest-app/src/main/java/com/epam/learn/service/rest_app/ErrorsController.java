package com.epam.learn.service.rest_app;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorsController implements ErrorController{

    @GetMapping("/error")
    ResponseEntity<Object> errorHandler(){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Override
    public String getErrorPath() {
        return null;
    }
}
