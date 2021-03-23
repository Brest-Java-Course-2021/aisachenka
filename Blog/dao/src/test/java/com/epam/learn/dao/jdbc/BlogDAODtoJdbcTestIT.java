package com.epam.learn.dao.jdbc;

import com.epam.learn.dao.BlogDAO;
import com.epam.learn.dao.BlogDtoDAO;
import com.epam.learn.model.Post;
import com.epam.learn.model.dto.BlogDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml","classpath*:dao.xml"})
class BlogDAODtoJdbcTestIT {

    @Autowired
    BlogDtoDAO blogDtoDAO;

    @Test
    public void getAllBlogsWithMaxLikesTest(){
        List<BlogDTO> allBlogsWithMaxLikes = blogDtoDAO.getAllBlogsWithMaxLikes();
        assertTrue(allBlogsWithMaxLikes.get(1).getBlogId()==2);
        assertTrue(allBlogsWithMaxLikes.get(1).getMaxNumberOfLikes()==4454);

        assertTrue(allBlogsWithMaxLikes.get(2).getBlogId()==1);
        assertTrue(allBlogsWithMaxLikes.get(2).getMaxNumberOfLikes()==3131);
    }
}