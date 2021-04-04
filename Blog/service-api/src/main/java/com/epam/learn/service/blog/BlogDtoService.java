package com.epam.learn.service.blog;

import com.epam.learn.model.dto.BlogDTO;

import java.util.List;
/**
 * Layer of abstraction that allows us to cut in half application and now it simply call the respective
 * methods of BlogDtoDAO
 * */
public interface BlogDtoService {
    List<BlogDTO> getAllBlogsWithMaxLikes();
}
