package com.epam.learn.service.post;

import com.epam.learn.model.Post;

import java.util.List;
import java.util.Optional;
/**
 * Layer of abstraction that allows us to cut application in half and now it simply call the respective
 * methods of PostDAO
 * */
public interface PostService {

    List<Post> findAll();

    Optional<Post> findById(Integer id);

    Integer create(Post post);

    Integer update(Post post);

    Integer delete(Integer id);

}
