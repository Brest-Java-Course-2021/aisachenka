package com.epam.learn.dao.post;


import com.epam.learn.model.dto.PostDTO;

import java.util.List;

public interface PostDtoDAO {
    List<PostDTO> findAllWithBlogName();

}
