package com.epam.learn.service.web_app.exeption;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllerAdvisor {
//`    @ExceptionHandler(RuntimeException.class)
//    public ModelAndView handle(Exception ex) {
//
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("message", ex.getMessage());
//        mv.setViewName("error");
//
//        return mv;
//    }`
}
