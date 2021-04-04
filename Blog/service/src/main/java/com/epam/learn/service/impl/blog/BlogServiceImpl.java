package com.epam.learn.service.impl.blog;

import com.epam.learn.dao.blog.BlogDAO;
import com.epam.learn.model.Blog;
import com.epam.learn.service.blog.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlogServiceImpl implements BlogService {

    private final BlogDAO blogDAO;

    @Autowired
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
