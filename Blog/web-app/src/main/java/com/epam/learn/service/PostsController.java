package com.epam.learn.service;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostsController {
    @GetMapping ("/posts")
    public String getAllPosts(){
        return "posts";
    }

    @GetMapping("/post/{id}")
    public String editPost(@PathVariable Integer id, Model model){
        return "post";
    }
}
