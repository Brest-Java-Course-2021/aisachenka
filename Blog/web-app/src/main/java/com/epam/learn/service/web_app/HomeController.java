package com.epam.learn.service.web_app;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String index() {
        return "redirect:blogs";
    }
}
