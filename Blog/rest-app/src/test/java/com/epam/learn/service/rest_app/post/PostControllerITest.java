package com.epam.learn.service.rest_app.post;

import com.epam.learn.model.Post;
import com.epam.learn.service.rest_app.PostController;
import com.epam.learn.service.rest_app.exception.ControllerAdvisor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostControllerITest.class);

    private static final String POST_ENDPOINT = "/posts";

    @Autowired
    PostController postController;

    @Autowired
    private ControllerAdvisor controllerAdvisor;

    // to make jackson work with LocalDate https://github.com/FasterXML/jackson-modules-java8/tree/master/datetime
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    protected MockMvc mockMvc;

    PostMockService postService = new PostMockService();

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(postController)
                .setControllerAdvice(controllerAdvisor)
                .alwaysDo(print())
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    void shouldFindAllPosts() throws Exception {
        LOGGER.debug("shouldFindAllPosts()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for (Post post : posts) {
            assertNotNull(post.getBlogName());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }
    }

    @Test
    void shouldFindPostById() throws Exception {
        LOGGER.debug("shouldFindPostById()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for (Post post : posts) {
            assertNotNull(post.getBlogName());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        Optional<Post> post = postService.findById(1);

        assertTrue(post.isPresent());
        Post expected = new Post("kitty", "Cute kitty", LocalDate.of(2018, 1, 1), 123);
        expected.setPostId(1);
        assertEquals(expected, post.get());

    }

    @Test
    void shouldCreatePost() throws Exception {
        LOGGER.debug("shouldCreatePost()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for (Post post : posts) {
            assertNotNull(post.getBlogName());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        Post expectedPost = new Post("kitty", "blah blah", LocalDate.of(2021, 3, 14), 4321);

        Integer createdPostId = postService.create(expectedPost);
        assertTrue(createdPostId > 0);
        Optional<Post> receivedPost = postService.findById(createdPostId);
        assertTrue(receivedPost.isPresent());
        expectedPost.setPostId(createdPostId);
        assertEquals(expectedPost, receivedPost.get());
    }

    @Test
    void shouldUpdatePost() throws Exception {
        LOGGER.debug("shouldUpdatePost()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for (Post post : posts) {
            assertNotNull(post.getBlogName());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        Post expectedPost = createPost(1, "kitty", "Test", 1337, LocalDate.of(2012, 12, 12));

        Integer numberOfUpdatedRows = postService.update(expectedPost);

        assertTrue(numberOfUpdatedRows > 0);
        Optional<Post> receivedPost = postService.findById(expectedPost.getPostId());
        assertTrue(receivedPost.isPresent());
        assertEquals(expectedPost, receivedPost.get());
    }


    @Test
    void shouldDelete() throws Exception {
        LOGGER.debug("shouldUpdatePost()");
        List<Post> posts = postService.findAll();
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for (Post post : posts) {
            assertNotNull(post.getBlogName());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        Integer id = 1;
        Integer numberOfDeletedValues = postService.delete(id);
        assertEquals(numberOfDeletedValues, 1);
    }

    @Test
    void searchByTwoDatesNormalTest() throws Exception {
        LOGGER.debug("searchByTwoDatesTest()");
        List<Post> posts = postService.searchByTwoDates(LocalDate.of(2018, 1, 1), LocalDate.of(2020, 1, 1));
        assertNotNull(posts);
        assertTrue(posts.size() > 0);
        for (Post post : posts) {
            assertNotNull(post.getBlogName());
            assertNotNull(post.getPostId());
            assertNotNull(post.getNumberOfLikes());
            assertNotNull(post.getText());
            assertNotNull(post.getLocalDate());
        }

        assertTrue(posts.contains(createPost(1, "kitty", "Cute kitty", 123, LocalDate.of(2018, 1, 1))));
        assertTrue(posts.contains(createPost(2, "kitty", "Cute asdads", 3131, LocalDate.of(2019, 10, 23))));

    }

    Post createPost(Integer postId, String blogName, String text, Integer numberOfLikes, LocalDate date) {
        Post post = new Post(blogName, text, date, numberOfLikes);
        post.setPostId(postId);
        return post;
    }


    class PostMockService {

        public List<Post> findAll() throws Exception {
            LOGGER.debug("findAll()");
            MockHttpServletResponse response = mockMvc.perform(get(new URI(POST_ENDPOINT))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);
            String responseString = response.getContentAsString();

            byte[] bytes = responseString.getBytes(StandardCharsets.ISO_8859_1);
            responseString = new String(bytes, StandardCharsets.UTF_8);

            return mapper.readValue(responseString, new TypeReference<List<Post>>() {
            });
        }

        public Optional<Post> findById(Integer id) throws Exception {
            LOGGER.debug("findById() {}", id);
            MockHttpServletResponse response = mockMvc.perform(get(new URI(POST_ENDPOINT + "/" + id)).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andReturn().getResponse();
            assertNotNull(response);
            return Optional.ofNullable(mapper.readValue(response.getContentAsString(), Post.class));
        }

        public Integer create(Post post) throws Exception {
            LOGGER.debug("create() {}", post);
            String json = mapper.writeValueAsString(post);
            MockHttpServletResponse response = mockMvc.perform(post(POST_ENDPOINT).
                    contentType(MediaType.APPLICATION_JSON).
                    content(json).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse();

            assertNotNull(response);

            return mapper.readValue(response.getContentAsString(), Integer.class);
        }

        public Integer update(Post post) throws Exception {
            LOGGER.debug("update() {}", post);
            String json = mapper.writeValueAsString(post);
            MockHttpServletResponse response = mockMvc.perform(put(POST_ENDPOINT).
                    contentType(MediaType.APPLICATION_JSON).
                    content(json).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();

            assertNotNull(response);

            return mapper.readValue(response.getContentAsString(), Integer.class);

        }

        public Integer delete(Integer id) throws Exception {
            LOGGER.debug("delete() {}", id);
            MockHttpServletResponse response = mockMvc
                    .perform(MockMvcRequestBuilders.delete(new URI(POST_ENDPOINT + "/" + id))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);
            return mapper.readValue(response.getContentAsString(), Integer.class);

        }

        public List<Post> searchByTwoDates(LocalDate dateBefore, LocalDate dateAfter) throws Exception {
            LOGGER.debug("searchByTwoDates() {} {}", dateBefore, dateAfter);
            MockHttpServletResponse response = mockMvc.perform(get(POST_ENDPOINT + "/search")
                    .param("dateBefore", dateBefore.toString())
                    .param("dateAfter", dateAfter.toString()).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andReturn().getResponse();

            return mapper.readValue(response.getContentAsString(), new TypeReference<List<Post>>() {
            });
        }
    }
}