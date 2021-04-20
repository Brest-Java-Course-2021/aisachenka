package com.epam.learn.service.blog;

import com.epam.learn.model.Blog;

import java.util.List;
import java.util.Optional;

/**
 * Layer of abstraction that allows us to cut application in half and now it simply call the respective
 * methods of BlogDAO
 */
public interface BlogService {

    List<Blog> findAll();

    Optional<Blog> findById(Integer blogId);

    Integer create(Blog blog);

    Integer update(Blog blog);

    Integer delete(Integer blogId);
}
