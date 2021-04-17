package com.epam.learn.service.web_app;

import com.epam.learn.model.Blog;
import com.epam.learn.model.Post;
import com.epam.learn.service.blog.BlogService;
import com.epam.learn.service.post.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class PostsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostsController.class);

    PostService postService;
    BlogService blogService;

    @Autowired
    public PostsController(PostService postService, BlogService blogService) {
        this.postService = postService;
        this.blogService = blogService;
    }

    @GetMapping ("/posts")
    public String getAllPosts(Model model){
        LOGGER.debug("getAllPosts()");
        List<Post> postList = postService.findAll();
        model.addAttribute("posts", postList);
        return "posts";
    }

    @GetMapping("/post/{id}")
    public String getFormOfEditingPost(@PathVariable Integer id, Model model){
        LOGGER.debug("getFormOfEditingPost() {}", id);
        List<Blog> allBlogs = blogService.findAll();
        Optional<Post> optionalPost = postService.findById(id);
        model.addAttribute("blogs",allBlogs);
        model.addAttribute("postModel",optionalPost.get());
        return "post";
    }

    @GetMapping("/post/add")
    public String getFormOfAddingPost(Model model){
        LOGGER.debug("getFormOfAddingPost()");
        List<Blog> allBlogs = blogService.findAll();
        model.addAttribute("blogs",allBlogs);
        model.addAttribute("postModel", new Post());
        return "newPost";
    }

    @PostMapping("/post/{id}")
    public String editPost(@PathVariable Integer id, Post post){
        LOGGER.debug("editPost() {} {}", id, post);
        postService.update(post);
        return "redirect:/posts";
    }

    @PostMapping("/post/add")
    public String addPost(Post post){
        LOGGER.debug("editPost() {} {}", post);
        postService.create(post);
        return "redirect:/posts";
    }

    @GetMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Integer id){
        LOGGER.debug("deletePost() {}", id);
        postService.delete(id);
        return "redirect:/posts";
    }

    @GetMapping("/search")
    public String filterPostsByDate(@RequestParam(value = "dateBefore",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateBefore,
                                    @RequestParam(value = "dateAfter", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateAfter,
                                    Model model){
        LOGGER.debug("filterPostsByDate() {} {}", dateBefore, dateAfter);
        List<Post> postList = postService.searchByTwoDates(dateBefore,dateAfter);
        model.addAttribute("posts", postList);
        return "posts";
    }

}
