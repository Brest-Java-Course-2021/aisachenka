package com.epam.learn.service.rest.config;

import com.epam.learn.service.blog.BlogDtoService;
import com.epam.learn.service.blog.BlogService;
import com.epam.learn.service.post.PostService;
import com.epam.learn.service.rest.blog.BlogDTOServiceRest;
import com.epam.learn.service.rest.blog.BlogServiceRest;
import com.epam.learn.service.rest.post.PostRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {
    public static final String BLOG_URL = "http://localhost:8090/blogs";
    public static final String BLOG_DTO_URL = "http://localhost:8090/blogs-dto";
    public static final String POST_URL = "http://localhost:8090/posts";

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    BlogService getBlogService() {
        return new BlogServiceRest(BLOG_URL, getRestTemplate());
    }

    @Bean
    BlogDtoService getBlogDtoService() {
        return new BlogDTOServiceRest(BLOG_DTO_URL, getRestTemplate());
    }

    @Bean
    PostService postService() {
        return new PostRestClient(POST_URL, getRestTemplate());
    }

}
