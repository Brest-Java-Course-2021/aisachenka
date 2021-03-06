package com.epam.learn.service.web_app;

import com.epam.learn.model.Blog;
import com.epam.learn.service.blog.BlogDtoService;
import com.epam.learn.service.blog.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String getBlogs(Model model) {
        LOGGER.debug("getBlogs()");
        model.addAttribute("blogs", blogDtoService.getAllBlogsWithMaxLikes());
        return "blogs";
    }

    @GetMapping(value = "blog/{id}")
    public String getFormOfEditBlog(@PathVariable Integer id, Model model) {
        LOGGER.debug("getFormOfEditBlog() {} {}", id, model);
        Optional<Blog> optionalBlog = blogService.findById(id);
        model.addAttribute("blog", optionalBlog.get());
        return "blog";

    }

    @GetMapping(value = "blog/add")
    public String getFormOfaddBlog(Model model) {
        model.addAttribute("blog", new Blog());
        return "newBlog";
    }

    @GetMapping(value = "blog/{id}/delete")
    public String deleteBlog(@PathVariable Integer id) {
        LOGGER.debug("deleteBlog() id={}", id);
        // todo: допилить js для delete, a может и не допилить
        blogService.delete(id);
        return "redirect:/blogs";
    }

    @PostMapping(value = "blog/add")
    public String addBlog(Blog blog) {
        LOGGER.debug("addBlog() {}", blog);
        blogService.create(blog);
        return "redirect:/blogs";
    }

    @PostMapping(value = "blog/{id}")
    public String editBlog(Blog blog) {
        LOGGER.debug("editBlog() {}", blog);
        blogService.update(blog);
        return "redirect:/blogs";
    }
}
