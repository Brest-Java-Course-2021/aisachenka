package com.epam.learn.service.rest_app.blog;

import com.epam.learn.model.Blog;
import com.epam.learn.service.rest_app.BlogsController;
import com.epam.learn.service.rest_app.exception.ControllerAdvisor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BlogsControllerITest {

    private final static Logger LOGGER = LoggerFactory.getLogger(BlogsControllerITest.class);

    private static final String BLOGS_ENDPOINT = "/blogs";

    @Autowired
    BlogsController blogsController;

    @Autowired
    private ControllerAdvisor controllerAdvisor;


    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    MockBlogService blogService = new MockBlogService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(blogsController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(controllerAdvisor)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllBlogs() throws Exception {
        LOGGER.debug("shouldReturnAllBlogs()");
        List<Blog> blogs = blogService.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);

        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }
    }

    @Test
    void shouldReturnBlogById() throws Exception {
        LOGGER.debug("shouldReturnBlogById()");
        List<Blog> blogs = blogService.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }


        Integer blogId = blogs.get(2).getBlogId();
        Blog blog = blogService.findById(blogId).get();
        assertEquals(blogId, blog.getBlogId());
        assertEquals(blogs.get(2).getBlogName(), blog.getBlogName());
        assertEquals(blog, blogs.get(2));

    }

    @Test
    void shouldCreateBlog() throws Exception {
        LOGGER.debug("shouldCreateBlog()");
        List<Blog> blogs = blogService.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

        Blog blog = new Blog("Emptiness");
        blogService.create(blog);

        List<Blog> blogsAfterAdding = blogService.findAll();
        assertEquals(blogsAfterAdding.size(), blogs.size() + 1);
    }

    @Test
    void shouldUpdateBlog() throws Exception {
        LOGGER.debug("shouldUpdateBlog()");
        List<Blog> blogs = blogService.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

        Blog blog = blogs.get(0);
        String name = RandomStringUtils.randomAlphabetic(50);
        Integer blogId = blog.getBlogId();
        blog.setBlogName(name);
        blogService.update(blog);
        assertNotNull(blogService.findById(blogId));
        String receivedBlogName = blogService.findById(blogId).get().getBlogName();
        assertEquals(name, receivedBlogName);
    }

    @Test
    void shouldDeleteBlogById() throws Exception {
        LOGGER.debug("delete()");
        List<Blog> blogs = blogService.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }


        List<Blog> blogsAfterAdding = blogService.findAll();
        Integer sizeOfAllBlogs = blogsAfterAdding.size();

        Integer deletedCount = blogService.delete(3);
        List<Blog> blogsAfterDeleting = blogService.findAll();
        assertEquals(sizeOfAllBlogs, blogsAfterDeleting.size() + 1);
        assertTrue(deletedCount == 1);

    }



    @Test
    void shouldReturnUnprocessableEntityAndErrorMessageWhenWeUpdateWithRepeatableValues() throws Exception {
        LOGGER.debug("shouldReturnUnprocessableEntityAndErrorMessage()");
        Blog blogWithOriginalName = blogService.findById(1).get();
        Blog blogThatRepeatsName = blogService.findById(2).get();
        blogThatRepeatsName.setBlogName(blogWithOriginalName.getBlogName());

        String json = objectMapper.writeValueAsString(blogThatRepeatsName);
        MockHttpServletResponse response = mockMvc.perform(put(BLOGS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
    }

    @Test
    void shouldReturnUnprocessableEntityAndErrorMessageWhenWeCreateWithRepeatableValues() throws Exception {
        LOGGER.debug("shouldReturnUnprocessableEntityAndErrorMessage()");
        blogService.create(new Blog("Blog"));

        String json = objectMapper.writeValueAsString(new Blog("Blog"));
        MockHttpServletResponse response = mockMvc.perform(post(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
    }



    @Test
    void shouldReturnNotFoundWhenWeTryToFindNotexistentBlog() throws Exception {
        LOGGER.debug("shouldReturnNotFoundWhenWeTryToFindInexistentBlog()");

        mockMvc.perform(get(BLOGS_ENDPOINT+"/"+245325)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenWeTryToDeleteNotexistentBlog() throws Exception {
        LOGGER.debug("shouldReturnNotFoundWhenWeTryToFindInexistentBlog()");

        mockMvc.perform(delete(BLOGS_ENDPOINT+"/"+245325)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void createDepartmentWithSameNameDiffCaseTest() throws Exception {
        LOGGER.debug("createDepartmentWithSameNameDiffCaseTest()");

        blogService.create(new Blog("dog"));
        Blog blog = new Blog("Dog");
        String json = objectMapper.writeValueAsString(blog);

        MockHttpServletResponse response = mockMvc.perform(post(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
    }

    @Test
    public void updateDepartmentWithSameNameDiffCaseTest() throws Exception {
        LOGGER.debug("createDepartmentWithSameNameDiffCaseTest()");

        blogService.create(new Blog("dog"));
        Blog blog = new Blog("Dog");
        blog.setBlogId(3);
        String json = objectMapper.writeValueAsString(blog);

        MockHttpServletResponse response = mockMvc.perform(put(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andReturn().getResponse();

        assertNotNull(response);
    }

    private class MockBlogService{

        public List<Blog> findAll() throws Exception {
            LOGGER.debug("findAll()");
            MockHttpServletResponse response = mockMvc.perform(get(BLOGS_ENDPOINT)
            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            String responseString = response.getContentAsString();

            byte[] bytes = responseString.getBytes(StandardCharsets.ISO_8859_1);
            responseString = new String(bytes, StandardCharsets.UTF_8);


            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Blog>>() {});
        }

        public Optional<Blog> findById(Integer blogId) throws Exception {
            LOGGER.debug("findById()");
            MockHttpServletResponse response = mockMvc.perform(get(BLOGS_ENDPOINT+"/"+blogId)
                    .characterEncoding("UTF-8")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse();
            String responseString = response.getContentAsString();

            byte[] bytes = responseString.getBytes(StandardCharsets.ISO_8859_1);
            responseString = new String(bytes, StandardCharsets.UTF_8);

            return Optional.ofNullable(objectMapper.readValue(responseString, Blog.class));
        }

        public Integer create(Blog blog) throws Exception {
            LOGGER.debug("create() {}", blog);
            String json = objectMapper.writeValueAsString(blog);
            MockHttpServletResponse response = mockMvc.perform(post(BLOGS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(),Integer.class);
        }

        public Integer update(Blog blog) throws Exception {
            LOGGER.debug("update() {}", blog);
            String json = objectMapper.writeValueAsString(blog);
            MockHttpServletResponse response = mockMvc.perform(put(BLOGS_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .characterEncoding("UTF-8")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(),Integer.class);
        }


        public Integer delete(Integer blogId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.
                    delete(BLOGS_ENDPOINT + "/" + blogId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Integer.class);

        }
    }
}