package com.epam.learn.service.rest;

import com.epam.learn.model.Blog;
import com.epam.learn.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

public class BlogServiceRest implements BlogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogServiceRest.class);

    private String url;

    private BlogService blogService;

    private RestTemplate restTemplate;

    @Autowired
    public BlogServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Blog> findAll() {
        LOGGER.debug("findAll()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url,List.class);
        return (List<Blog>) responseEntity.getBody();
    }

    @Override
    public Optional<Blog> findById(Integer blogId) {
        LOGGER.debug("findById() {}", blogId);
        ResponseEntity<Blog> responseEntity = restTemplate.getForEntity(url + "/" + blogId, Blog.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Integer create(Blog blog) {
        LOGGER.debug("create() {}", blog);
        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(url, blog, Integer.class);
        return responseEntity.getBody();
    }

    @Override
    public Integer update(Blog blog) {
        LOGGER.debug("update() {}", blog);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Blog> httpEntity = new HttpEntity<>(blog,headers);
        ResponseEntity<Integer> responseEntity = restTemplate.exchange(url, HttpMethod.PUT,httpEntity,Integer.class);

        return responseEntity.getBody();
    }

    @Override
    public Integer delete(Integer blogId) {
        LOGGER.debug("delete() {}", blogId);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity<>(headers);

        ResponseEntity<Integer> responseEntity = restTemplate
                .exchange(url + "/" + blogId, HttpMethod.DELETE, entity, Integer.class);

        return responseEntity.getBody();
    }
}
