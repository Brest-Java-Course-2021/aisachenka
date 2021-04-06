package com.epam.learn.service.post;

import com.epam.learn.model.dto.PostDTO;

import java.util.List;

public interface PostDtoService{
    List<PostDTO> findAllWithBlogName();

}
