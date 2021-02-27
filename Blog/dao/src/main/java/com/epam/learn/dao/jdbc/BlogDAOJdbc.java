package com.epam.learn.model.dao.jdbc;


import com.epam.learn.model.Blog;
import com.epam.learn.model.dao.BlogDAO;

import java.util.List;
import java.util.Optional;

public class BlogDAOJdbc implements BlogDAO {
    @Override
    public List<Blog> findAll() {
        return null;
    }

    @Override
    public Optional<Blog> findById(Integer blogId) {
        return Optional.empty();
    }

    @Override
    public Integer create(Blog blog) {
        return null;
    }

    @Override
    public Integer update(Blog blog) {
        return null;
    }

    @Override
    public Integer delete(Integer blogId) {
        return null;
    }
}
