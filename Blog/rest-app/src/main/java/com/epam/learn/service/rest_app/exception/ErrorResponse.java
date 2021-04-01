package com.epam.learn.service.rest_app.exception;

import java.util.List;

public class ErrorResponse {
    private List<String> errors;

    public ErrorResponse() { }

    public ErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
