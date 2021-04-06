package com.epam.learn.service.impl.post;

import com.epam.learn.model.dto.PostDTO;
import com.epam.learn.service.impl.post.config.ServiceTestConfig;
import com.epam.learn.service.post.PostDtoService;
import com.epam.learn.testdb.SpringJdbcConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringJdbcConfig.class, ServiceTestConfig.class })
@Transactional
public class PostDtoServiceImplITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImplITest.class);

    @Autowired
    PostDtoService postDtoService;

    @Test
    void shouldFindAllPostsWithBlogName() {
        LOGGER.debug("shouldFindAllPostsWithBlogName()");
        List<PostDTO> posts = postDtoService.findAllWithBlogName();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for(PostDTO post: posts){
            assertNotNull(post.getBlogName());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }
    }
}
