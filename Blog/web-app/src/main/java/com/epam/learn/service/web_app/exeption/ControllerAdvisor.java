package com.epam.learn.service.web_app.exeption;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@ControllerAdvice
public class ControllerAdvisor {


    private ObjectMapper mapper = new ObjectMapper();

   @ExceptionHandler(HttpClientErrorException.class)
    public String handle(Exception ex, Model model) throws JsonProcessingException {

        ModelAndView mv = new ModelAndView();
        String exception = ex.getMessage();
        exception = new StringBuilder(exception).substring(7,exception.length()-1);
        model.addAttribute("errors", mapper.readValue( exception, ErrorResponse.class).getErrors() );


        return "error";
    }
}
