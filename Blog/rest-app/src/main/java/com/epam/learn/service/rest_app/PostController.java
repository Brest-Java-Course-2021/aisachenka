package com.epam.learn.service.rest_app;

import com.epam.learn.model.Post;
import com.epam.learn.service.post.PostService;
import com.epam.learn.service.rest_app.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    private static final Logger LOGGER= LoggerFactory.getLogger(PostController.class);

    PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    List<Post> getAllPosts(){
        LOGGER.debug("getAllPosts()");
        return postService.findAll();
    }

    @GetMapping("/posts/{id}")
    ResponseEntity<Object> getPostById(@PathVariable Integer id){
        LOGGER.debug("getPostById() {}", id);
        Optional<Post> postOptional = postService.findById(id);
        return postOptional.isPresent()
                ? new ResponseEntity<>(postOptional.get(), HttpStatus.OK)
                : new ResponseEntity<>(new ErrorResponse(List.of("Can't find Post with such id")), HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/posts", consumes = "application/json", produces = "application/json")
    ResponseEntity<Integer> createPost(@Valid @RequestBody Post post){
        LOGGER.debug("getPostById() {}", post);
        return new ResponseEntity<>(postService.create(post), HttpStatus.CREATED);
    }


    //TODO: make normal date validator
    @PutMapping(value = "/posts", consumes = "application/json", produces = "application/json")
    ResponseEntity<Object> updatePost(@Valid @RequestBody Post post){
        LOGGER.debug("updatePost() {}", post);
        Integer numberOfUpdatedPosts = postService.update(post);
        return numberOfUpdatedPosts > 0
                ? new ResponseEntity<>(numberOfUpdatedPosts, HttpStatus.OK)
                : new ResponseEntity<>(new ErrorResponse(List.of("Can't update Post with such id")), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/posts/{id}")
    ResponseEntity<Object> deletePost(@PathVariable Integer id){
        LOGGER.debug("deletePost() {}", id);
        Integer numberOfDeletedPosts = postService.delete(id);
        return numberOfDeletedPosts > 0
                ? new ResponseEntity<>(numberOfDeletedPosts, HttpStatus.OK)
                : new ResponseEntity<>(new ErrorResponse(List.of("Can't delete Post with such id")), HttpStatus.NOT_FOUND);
    }


    @GetMapping("/posts/search")
    List<Post> getPostBetweenTwoDates(
            @RequestParam(value = "dateBefore", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate dateBefore,
            @RequestParam(value = "dateAfter", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate dateAfter) {
        LOGGER.debug("getPostBetweenTwoDates() before={} after={}", dateBefore, dateAfter);

        if(dateBefore == null)
            dateBefore = LocalDate.of(LocalDate.MIN.getYear(), 1,1);

        if(dateAfter ==null)
            dateAfter = LocalDate.of(LocalDate.MAX.getYear(), 1,1);

        return postService.searchByTwoDates(dateBefore,dateAfter);
    }

}
