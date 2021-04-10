package com.epam.learn.service.rest.post;

import com.epam.learn.model.Post;
import com.epam.learn.service.post.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PostRestClient implements PostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostRestClient.class);

    private String url;
    private RestTemplate restTemplate;


    public PostRestClient(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }



    @Override
    public List<Post> findAll() {
        LOGGER.debug("findAll()");
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {}).getBody();
    }

    @Override
    public Optional<Post> findById(Integer id) {
        LOGGER.debug("findById() {}", id);
        return Optional.ofNullable(restTemplate.exchange(url + "/" + id,
                HttpMethod.GET, null, new ParameterizedTypeReference<Post>() {}).getBody());
    }

    @Override
    public Integer create(Post post) {
        LOGGER.debug("create() {}",post);
        return restTemplate.postForEntity(url,post,Integer.class).getBody();
    }

    @Override
    public Integer update(Post post) {
        LOGGER.debug("update() {}", post);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Post> httpEntity = new HttpEntity<>(post,headers);
        ResponseEntity<Integer> responseEntity = restTemplate.exchange(url, HttpMethod.PUT,httpEntity,Integer.class);
        return responseEntity.getBody();
    }

    @Override
    public Integer delete(Integer id) {
        LOGGER.debug("delete() {}", id);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Post> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer> responseEntity = restTemplate.exchange(url+"/"+id, HttpMethod.DELETE,httpEntity,Integer.class);
        return responseEntity.getBody();
    }

    @Override
    public List<Post> searchByTwoDates(LocalDate dateBefore, LocalDate dateAfter) {
        LOGGER.debug("searchByTwoDates() {} {}",dateBefore,dateAfter);
        String dateBeforeString = dateBefore == null? "":dateBefore.toString();
        String dateAfterString = dateAfter == null? "":dateAfter.toString();


        String searchUrl = new StringBuilder(url)
                .append("/search?dateBefore=").append(dateBeforeString)
                .append("&dateAfter=").append(dateAfterString)
                .toString();
        return restTemplate.exchange(searchUrl, HttpMethod.GET,null, new ParameterizedTypeReference<List<Post>>(){}).getBody();
    }
}
