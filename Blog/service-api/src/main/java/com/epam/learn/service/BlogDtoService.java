package com.epam.learn.service;

import com.epam.learn.model.dto.BlogDTO;

import java.util.List;

public interface BlogDtoService {
    List<BlogDTO> getAllBlogsWithMaxLikes();
}
