package com.epam.learn.service.web_app;

import com.epam.learn.service.BlogDtoService;
import com.epam.learn.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BlogsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogsController.class);

    BlogService blogService;
    BlogDtoService blogDtoService;

    @Autowired
    public BlogsController(BlogService blogService, BlogDtoService blogDtoService) {
        this.blogService = blogService;
        this.blogDtoService = blogDtoService;
    }

    @GetMapping("/blogs")
    public String getBlogs(Model model){
        LOGGER.debug("getBlogs()");
        model.addAttribute("blogs", blogDtoService.getAllBlogsWithMaxLikes());
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
