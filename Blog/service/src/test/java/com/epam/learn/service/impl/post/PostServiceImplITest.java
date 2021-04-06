package com.epam.learn.service.impl.post;

import com.epam.learn.dao.jdbc.exeption.SuchBlogNotExistsException;
import com.epam.learn.model.Post;
import com.epam.learn.service.impl.post.config.ServiceTestConfig;
import com.epam.learn.service.post.PostService;
import com.epam.learn.testdb.SpringJdbcConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringJdbcConfig.class, ServiceTestConfig.class, })
@Transactional
class PostServiceImplITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImplITest.class);

    @Autowired
    PostService postService;

    @Test
    void shouldFindAllPosts() {
        LOGGER.debug("shouldFindAllPosts()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for(Post post: posts){
            assertNotNull(post.getBlogId());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }
    }

    @Test
    void shouldFindPostById() {
        LOGGER.debug("shouldFindPostById()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for(Post post: posts){
            assertNotNull(post.getBlogId());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        Optional<Post> post = postService.findById(1);

        assertTrue(post.isPresent());
        Post expected = new Post( 1, "Cute kitty", LocalDate.of(2018,1,1),123);
        expected.setPostId(1);
        assertEquals( expected,post.get());

    }

    @Test
    void shouldCreatePost() {
        LOGGER.debug("shouldCreatePost()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for(Post post: posts){
            assertNotNull(post.getBlogId());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        Post expectedPost =  new Post (1, "blah blah", LocalDate.of(2021,3,14), 4321);

        Integer createdPostId = postService.create(expectedPost);
        assertTrue(createdPostId > 0);
        Optional<Post> receivedPost = postService.findById(createdPostId);
        assertTrue(receivedPost.isPresent());
        expectedPost.setPostId(createdPostId);
        assertEquals(expectedPost, receivedPost.get());
    }

    @Test
    void createWithNonexistentBlogExceptionalTest() {
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for(Post post: posts){
            assertNotNull(post.getBlogId());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        Post expectedPost = createPost(1,99999, "blah blah", 4321, LocalDate.of(2021,3,14));

        assertThrows(SuchBlogNotExistsException.class,()->{
            Integer updatedPosts = postService.create(expectedPost);
        });
    }

    @Test
    void shouldUpdatePost() {
        LOGGER.debug("shouldUpdatePost()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for(Post post: posts){
            assertNotNull(post.getBlogId());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        Post expectedPost = createPost(1,2, "Test", 1337, LocalDate.of(2012,12,12));

        Integer numberOfUpdatedRows = postService.update(expectedPost);

        assertTrue(numberOfUpdatedRows > 0);
        Optional<Post> receivedPost = postService.findById(expectedPost.getPostId());
        assertTrue(receivedPost.isPresent());
        assertEquals(expectedPost, receivedPost.get());
    }

    @Test
    void shouldThrowExceptionWhenBlogIdNotFoundWhileUpdatePost() {
        LOGGER.debug("shouldUpdatePost()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for(Post post: posts){
            assertNotNull(post.getBlogId());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        Post expectedPost = createPost(1,99999, "Test", 1337, LocalDate.of(2012,12,12));

        assertThrows(SuchBlogNotExistsException.class,()->{
            postService.update(expectedPost);
        });
    }


    @Test
    void delete() {
        LOGGER.debug("shouldUpdatePost()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for(Post post: posts){
            assertNotNull(post.getBlogId());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        Integer id = 1;
        Integer numberOfDeletedValues = postService.delete(id);
        assertEquals(numberOfDeletedValues, 1);
        assertTrue(postService.findById(id).isEmpty());

    }

    @Test
    void searchByTwoDatesNormalTest() {
        LOGGER.debug("searchByTwoDatesTest()");
        List<Post> posts = postService.searchByTwoDates(LocalDate.of(2018,1,1),LocalDate.of(2020,1,1));
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for(Post post: posts){
            assertNotNull(post.getBlogId());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        assertTrue(posts.contains(createPost( 1, 1, "Cute kitty", 123, LocalDate.of(2018,1,1))));
        assertTrue(posts.contains(createPost( 2, 1, "Cute asdads", 3131, LocalDate.of(2019,10,23))));

    }

    @Test
    void searchByTwoDatesExceptionalTest() {
        LOGGER.debug("searchByTwoDatesTest()");
        assertThrows(IllegalArgumentException.class,()->{
            List<Post> posts = postService.searchByTwoDates(LocalDate.of(2020,1,1), LocalDate.of(2018,1,1));
            System.out.println("asda");
        });
    }

    Post createPost(Integer postId, Integer blogId, String text, Integer numberOfLikes, LocalDate date){
        Post post = new Post(blogId, text, date, numberOfLikes);
        post.setPostId(postId);
        return post;
    }
}