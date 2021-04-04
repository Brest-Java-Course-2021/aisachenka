package com.epam.learn.service.impl;

import com.epam.learn.dao.blog.BlogDtoDAO;
import com.epam.learn.model.dto.BlogDTO;
import com.epam.learn.service.BlogDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class BlogDtoServiceImpl implements BlogDtoService {


    BlogDtoDAO blogDtoDAO;

    @Autowired
    BlogDtoServiceImpl(BlogDtoDAO blogDtoDAO){
        this.blogDtoDAO = blogDtoDAO;
    }

    @Override
    public List<BlogDTO> getAllBlogsWithMaxLikes() {
        return blogDtoDAO.getAllBlogsWithMaxLikes();
    }
}
