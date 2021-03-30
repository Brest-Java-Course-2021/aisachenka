package com.epam.learn.service.rest_app;

import com.epam.learn.model.dto.BlogDTO;
import com.epam.learn.service.BlogDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlogsDTOController {

    BlogDtoService blogDtoService;

    @Autowired
    public BlogsDTOController(BlogDtoService blogDtoService) {
        this.blogDtoService = blogDtoService;
    }

    @GetMapping("/blogs-dto")
    public List<BlogDTO> getAllDTOBlogs(){
        return blogDtoService.getAllBlogsWithMaxLikes();
    }
}
