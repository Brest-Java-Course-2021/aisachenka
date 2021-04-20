package com.epam.learn.dao.blog;

import com.epam.learn.model.dto.BlogDTO;

import java.util.List;

public interface BlogDtoDAO {
    /**
     * this function call datasource and select all blogs with maximum number of likes and translate it in List
     *
     * @return list of all finded blogDTOs
     */
    List<BlogDTO> getAllBlogsWithMaxLikes();
}
