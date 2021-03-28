package com.epam.learn.service.rest_app.exception;

public class BlogNotFoundException extends RuntimeException{
    public BlogNotFoundException(Integer id) {
        super(String.format("Blog with id %d not found", id));
    }
}
