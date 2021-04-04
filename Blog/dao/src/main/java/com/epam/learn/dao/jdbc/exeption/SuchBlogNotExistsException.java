package com.epam.learn.dao.jdbc.exeption;

public class SuchBlogNotExistsException extends RuntimeException{
    public SuchBlogNotExistsException(String mes){
        super(mes);
    }
}
