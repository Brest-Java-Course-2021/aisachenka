package com.epam.learn.impl;

import com.epam.learn.dao.BlogDAO;
import com.epam.learn.model.Blog;
import com.epam.learn.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlogServiceImpl implements BlogService {

    private final BlogDAO blogDAO;

    public BlogServiceImpl(BlogDAO blogDAO) {
        this.blogDAO = blogDAO;
    }

    @Override
    public List<Blog> findAll() {
        return blogDAO.findAll();
    }

    @Override
    public Optional<Blog> findById(Integer blogId) {
        return blogDAO.findById(blogId);
    }

    @Override
    public Integer create(Blog blog) {
        return blogDAO.create(blog);
    }

    @Override
    public Integer update(Blog blog) {
        return blogDAO.update(blog);
    }

    @Override
    public Integer delete(Integer blogId) {
        return blogDAO.delete(blogId);
    }
}
