package com.epam.learn.dao.jdbc.exeption;

public class ConstraintException extends RuntimeException {
    public ConstraintException(String mes) {
        super(mes);
    }
}
