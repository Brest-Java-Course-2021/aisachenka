package com.epam.learn.dao;

import com.epam.learn.model.dto.BlogDTO;

import java.util.List;

public interface BlogDtoDAO {
    List<BlogDTO> getAllBlogsWithMaxLikes();
}
