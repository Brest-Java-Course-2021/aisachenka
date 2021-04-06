package com.epam.learn.service.impl.post;

import com.epam.learn.dao.post.PostDtoDAO;
import com.epam.learn.model.dto.PostDTO;
import com.epam.learn.service.post.PostDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostDtoServiceImpl implements PostDtoService {
    PostDtoDAO postDtoDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(PostDtoServiceImpl.class);

    @Autowired
    public PostDtoServiceImpl(PostDtoDAO postDtoDAO) {
        this.postDtoDAO = postDtoDAO;
    }

    @Override
    public List<PostDTO> findAllWithBlogName() {
        return postDtoDAO.findAllWithBlogName();
    }
}
