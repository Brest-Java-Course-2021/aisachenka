package com.epam.learn.service.web_app.config;

import com.epam.learn.service.blog.BlogDtoService;
import com.epam.learn.service.blog.BlogService;
import com.epam.learn.service.post.PostService;
import com.epam.learn.service.rest.blog.BlogDTOServiceRest;
import com.epam.learn.service.rest.blog.BlogServiceRest;
import com.epam.learn.service.rest.post.PostRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SpringConfig {

    @Value("${rest.server.port}")
    private Integer port;

    @Value("${rest.server.url}")
    private String url;

    @Value("${rest.server.protocol}")
    private String protocol;


    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    BlogService getBlogService(){
        return new BlogServiceRest(String.format("%s://%s:%d/blogs",protocol,url,port),getRestTemplate());
    }

    @Bean
    BlogDtoService getBlogDTOService(){
        return new BlogDTOServiceRest(String.format("%s://%s:%d/blogs-dto",protocol,url,port),getRestTemplate());
    }

    @Bean
    PostService postService(){
        return new PostRestClient(String.format("%s://%s:%d/posts",protocol,url,port),getRestTemplate());
    }




}
