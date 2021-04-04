package com.epam.learn.service.rest;

import com.epam.learn.model.Blog;
import com.epam.learn.service.blog.BlogService;
import com.epam.learn.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class BlogServiceRestTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(BlogServiceRestTest.class);

    public static final String BLOGS_URL = "http://localhost:8090/blogs";

    @Autowired
    BlogService blogService;

    @Autowired
    RestTemplate restTemplate;

    ObjectMapper mapper = new ObjectMapper();

    MockRestServiceServer mockServer;


    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldFindAllBlogs() throws Exception {
        LOGGER.debug("shouldFindAllBlogs()");
        // mocked URI with stubbed return parameters
        mockServer.expect(requestTo(new URI(BLOGS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(List.of(
                        getBlog(0),
                        getBlog(1)
                ))));


        // call of mocked URI
        List<Blog> blogs = blogService.findAll();

        // some tests of correct behavior

        mockServer.verify();

        assertNotNull(blogs);
        assertTrue(blogs.size()>0);
    }

    @Test
    void shouldFindBlogById() throws Exception{
        LOGGER.debug("shouldFindBlogById()");
        //given
        Integer id = 1;
        Blog blog = new Blog();
        blog.setBlogId(1);
        blog.setBlogName("blog "+ id);
        mockServer.expect(requestTo(new URI(BLOGS_URL+"/"+id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(blog)));
        // when
        Optional<Blog> optionalBlog = blogService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalBlog.isPresent());
        assertEquals(optionalBlog.get().getBlogId(), blog.getBlogId());
        assertEquals(optionalBlog.get().getBlogName(), blog.getBlogName());


    }

    @Test
    void shouldCreateBlog() throws Exception{
        LOGGER.debug("shouldCreateBlog()");
        // given
        Blog blog = new Blog("TEST");
        mockServer.expect(requestTo(new URI(BLOGS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString("1")));

        // when

        Integer id = blogService.create(blog);
        mockServer.verify();

        // then
        assertNotNull(id);

    }

    @Test
    void shouldUpdateBlog() throws Exception{
        LOGGER.debug("shouldUpdateBlog()");

        Blog blog = new Blog("TEST2");

        mockServer.expect(requestTo(new URI(BLOGS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString("1")));

        Integer update = blogService.update(blog);
        mockServer.verify();

        assertNotNull(update);
    }


    @Test
    void shouldDeleteBlog() throws Exception {
        LOGGER.debug("shouldDeleteBlog()");

        Integer blogId = 3;

        mockServer.expect(requestTo(new URI(BLOGS_URL + "/" +blogId)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1")));

        Integer update = blogService.delete(blogId);
        mockServer.verify();

        assertNotNull(update);
    }

    Blog getBlog(Integer id){
        Blog blog = new Blog();
        blog.setBlogId(id);
        blog.setBlogName("blog "+ id);
        return blog;
    }

}