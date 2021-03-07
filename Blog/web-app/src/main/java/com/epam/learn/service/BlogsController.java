package com.epam.learn.service;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BlogsController {

    @GetMapping("/blogs")
    public String getBlogs(Model model){
        return "blogs";
    }

    @GetMapping(value = "blog/{id}")
    public String editBlog(@PathVariable Integer id, Model model){
        return "blog";
    }

    @GetMapping(value = "blog/add")
    public String addBlog(Model model){
        return "blog";
    }

}
