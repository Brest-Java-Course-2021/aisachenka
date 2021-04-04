package com.epam.learn.dao.jdbc;

import com.epam.learn.dao.blog.BlogDtoDAO;
import com.epam.learn.model.dto.BlogDTO;
import com.epam.learn.testdb.SpringJdbcConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
@DataJdbcTest
@Import({BlogDAODtoJdbc.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@PropertySource({"classpath:dao.properties"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BlogDAODtoJdbcTestIT {

    public static final Logger LOGGER = LoggerFactory.getLogger(BlogDAODtoJdbcTestIT.class);

    @Autowired
    BlogDtoDAO blogDtoDAO;

    @Test
    public void getAllBlogsWithMaxLikesTest(){
        LOGGER.debug("getAllBlogsWithMaxLikesTest()");
        List<BlogDTO> allBlogsWithMaxLikes = blogDtoDAO.getAllBlogsWithMaxLikes();
        assertTrue(allBlogsWithMaxLikes.get(1).getBlogId()==2);
        assertTrue(allBlogsWithMaxLikes.get(1).getMaxNumberOfLikes()==4454);

        assertTrue(allBlogsWithMaxLikes.get(2).getBlogId()==1);
        assertTrue(allBlogsWithMaxLikes.get(2).getMaxNumberOfLikes()==3131);
    }
}