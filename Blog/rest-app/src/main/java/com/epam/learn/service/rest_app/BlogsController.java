package com.epam.learn.service.rest_app;

import com.epam.learn.model.Blog;
import com.epam.learn.service.blog.BlogService;
import com.epam.learn.service.rest_app.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class BlogsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogsController.class);

    BlogService blogService;

    @Autowired
    public BlogsController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blogs")
    public List<Blog> getBlogs() {
        LOGGER.debug("getBlogs()");
        return blogService.findAll();
    }

    @GetMapping("/blogs/{id}")
    public ResponseEntity<Object> getBlogById(@PathVariable Integer id) {
        LOGGER.debug("getBlogById() {}", id);
        Optional<Blog> optionalBlog = blogService.findById(id);
        return optionalBlog.isPresent()
                ? new ResponseEntity<>(optionalBlog.get(), HttpStatus.OK)
                : new ResponseEntity<>(new ErrorResponse(List.of("Can't find Blog with such id")), HttpStatus.NOT_FOUND);

    }


    @PostMapping(value = "/blogs", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Integer> createBlog(@Valid @RequestBody Blog blog) {
        LOGGER.debug("createBlog() {}", blog);
        return new ResponseEntity<>(blogService.create(blog), HttpStatus.CREATED);
    }

    @PutMapping(value = "/blogs", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Object> updateBlog(@Valid @RequestBody Blog blog) {
        LOGGER.debug("updateBlog() {}", blog);
        Integer count = blogService.update(blog);
        return count > 0
                ? new ResponseEntity<>(count, HttpStatus.OK)
                : new ResponseEntity<>(new ErrorResponse(List.of("Can't update Blog with such id")), HttpStatus.NOT_FOUND);

    }


    @DeleteMapping(value = "/blogs/{id}")
    public ResponseEntity<Object> deleteBlogById(@PathVariable Integer id) {
        LOGGER.debug("deleteBlogById() {}", id);
        Integer numberOfDeletedBlogs = blogService.delete(id);

        return numberOfDeletedBlogs > 0
                ? new ResponseEntity<>(numberOfDeletedBlogs, HttpStatus.OK)
                : new ResponseEntity<>(new ErrorResponse(List.of("Can't delete Blog with such id")), HttpStatus.NOT_FOUND);
    }


}
