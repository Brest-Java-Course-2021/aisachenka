package com.epam.learn.service.rest_app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private static final String VERSION = "1.0";

    @GetMapping("/version")
    public String getVersion(){
        return VERSION;
    }
}
