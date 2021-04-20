package com.epam.learn.service.rest.blog;

import com.epam.learn.model.dto.BlogDTO;
import com.epam.learn.service.blog.BlogDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;


public class BlogDTOServiceRest implements BlogDtoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogDTOServiceRest.class);

    private String url;

    private RestTemplate restTemplate;

    @Autowired
    public BlogDTOServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<BlogDTO> getAllBlogsWithMaxLikes() {
        LOGGER.debug("getAllBlogsWithMaxLikes()");
        return restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<List<BlogDTO>>() {
        }).getBody();
    }
}
