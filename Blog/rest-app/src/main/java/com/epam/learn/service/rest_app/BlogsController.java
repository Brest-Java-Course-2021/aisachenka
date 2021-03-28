package com.epam.learn.service.rest_app;

import com.epam.learn.model.Blog;
import com.epam.learn.service.BlogService;
import com.epam.learn.service.rest_app.exception.BlogNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Blog getBlogById(@PathVariable Integer id) {
        LOGGER.debug("getBlogById() {}", id);
        return blogService.findById(id).orElseThrow(() -> new BlogNotFoundException(id));
    }


    @PostMapping(value = "/blogs",consumes = {"application/json"},produces = {"application/json"})
    public ResponseEntity<Integer> createBlog(@RequestBody Blog blog){
        LOGGER.debug("createBlog() {}", blog);
        return new ResponseEntity<>(blogService.create(blog), HttpStatus.CREATED);
    }

    @PutMapping(value = "/blogs",consumes = {"application/json"},produces = {"application/json"})
    public ResponseEntity<Integer> updateBlog(@RequestBody Blog blog){
        LOGGER.debug("updateBlog() {}", blog);
        return new ResponseEntity<>(blogService.update(blog), HttpStatus.OK);
    }


    @DeleteMapping(value = "/blogs/{id}")
    public ResponseEntity<Integer>  deleteBlogById(@PathVariable Integer id) {
        LOGGER.debug("deleteBlogById() {}", id);
//        try{
            return new ResponseEntity<>(blogService.delete(id), HttpStatus.OK) ;
//        }catch (org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException e){
//            throw new
//        }
//        return blogService.delete(id);
    }


}
