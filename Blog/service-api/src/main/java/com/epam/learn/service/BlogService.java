package com.epam.learn.service;

import com.epam.learn.model.Blog;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    List<Blog> findAll();

    Optional<Blog> findById(Integer blogId);

    Integer create(Blog blog);

    Integer update(Blog blog);

    Integer delete(Integer blogId);
}
