package com.epam.learn.service.web_app;

import com.epam.learn.model.Blog;
import com.epam.learn.service.BlogDtoService;
import com.epam.learn.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

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
    public String getFormOfEditBlog(@PathVariable Integer id, Model model){
        LOGGER.debug("getFormOfEditBlog() {} {}", id, model);
        Optional<Blog> optionalBlog = blogService.findById(id);
        if(optionalBlog.isPresent()){
            model.addAttribute("blog",optionalBlog.get());
            return "blog";
        }
        LOGGER.warn("getFormOfEditBlog() such id doesn't exist exception {} {}", id, model);
        throw new IllegalArgumentException("Such id doesn't exists");
        //return "redirect:/error";
    }

    @GetMapping(value = "blog/add")
    public String getFormOfaddBlog(Model model){
        model.addAttribute("blog", new Blog());
        return "newBlog";
    }

    @PostMapping(value = "blog/add")
    public String addBlog(Blog blog){
        LOGGER.debug("addBlog() {}",blog);
        blogService.create(blog);
        return "redirect:/blogs";
    }

    @PostMapping(value = "blog/{id}")
    public String editBlog(Blog blog){
        LOGGER.debug("editBlog() {}", blog);
        blogService.update(blog);
        return "redirect:/blogs";
    }
}
