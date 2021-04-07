package com.epam.learn.service.rest_app.post;

import com.epam.learn.model.Post;
import com.epam.learn.service.rest_app.PostController;
import com.epam.learn.service.rest_app.exception.ControllerAdvisor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class PostControllerValidationITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostControllerValidationITest.class);

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

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(postController)
                .setControllerAdvice(controllerAdvisor)
                .alwaysDo(print())
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    void shouldNotFindPostByNonexistentId() throws Exception {
        Integer id = 99999;
        mockMvc.perform(get(POST_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Can't find Post with such id")));

    }

    @Test
    void shouldNotDeletePostByNonexistentId() throws Exception {
        Integer id = 99999;
        mockMvc.perform(delete(POST_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Can't delete Post with such id")));
    }

    @Test
    void shouldNotUpdatePostByNonexistentId() throws Exception {
        Post post = createPost(9999, "animals", RandomStringUtils.randomAlphabetic(250), 500, LocalDate.of(2020, 10, 1));
        String json = mapper.writeValueAsString(post);
        mockMvc.perform(put(POST_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Can't update Post with such id")));

    }

    @Test
    void validationOfNotExistingOfBlogWithSuchNameWhenCreating() throws Exception {
        Post post = new Post(RandomStringUtils.randomAlphabetic(50), RandomStringUtils.randomAlphabetic(250), LocalDate.of(2020, 10, 1), 500);
        String json = mapper.writeValueAsString(post);
        mockMvc.perform(post(POST_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Blog with such name doesn't exists")));
    }

    @Test
    void validationOfNotExistingOfBlogWithSuchNameWhenUpdating() throws Exception {
        Post post = createPost(1, RandomStringUtils.randomAlphabetic(50), RandomStringUtils.randomAlphabetic(250), 500, LocalDate.of(2020, 10, 1));
        String json = mapper.writeValueAsString(post);
        mockMvc.perform(put(POST_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Blog with such name doesn't exists")));
    }

    @Test
    void validationOfCorrectOrderWhenSearch() throws Exception {
        mockMvc.perform(get(new URI(POST_ENDPOINT + "/search"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("dateBefore", LocalDate.of(2020, 1, 1).toString())
                .param("dateAfter", LocalDate.of(2012, 1, 1).toString()))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Date After should be later than date before")));


    }

    @Test
    void validationCheckWhenUpdating() throws Exception {
        Post post = createPost(1, "animals", RandomStringUtils.randomAlphabetic(350), -12, LocalDate.of(2024, 10, 1));
        String json = mapper.writeValueAsString(post);
        mockMvc.perform(put(new URI(POST_ENDPOINT))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem("Date can not be future")))
                .andExpect(jsonPath("$.errors", hasItem("number of Likes should be greater or equal zero")))
                .andExpect(jsonPath("$.errors", hasItem("Text should be b-n 1 and 300 characters")));

    }

    @Test
    void validationCheckWhenUpdatingWithBlankValues() throws Exception {
        Post post = createPost(1, "", "", null, null);
        String json = mapper.writeValueAsString(post);
        mockMvc.perform(put(new URI(POST_ENDPOINT))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(5)))
                .andExpect(jsonPath("$.errors", hasItem("Please provide a date")))
                .andExpect(jsonPath("$.errors", hasItem("number of Likes is mandatory")))
                .andExpect(jsonPath("$.errors", hasItem("Post text is mandatory")))
                .andExpect(jsonPath("$.errors", hasItem("Blog name is mandatory")))
                .andExpect(jsonPath("$.errors", hasItem("Blog name should be b-n 2 and 50 characters")));
    }

    @Test
    void validationCheckWhenCreating() throws Exception {
        Post post = new Post("animals", RandomStringUtils.randomAlphabetic(350), LocalDate.of(2024, 10, 1), -12);
        String json = mapper.writeValueAsString(post);
        mockMvc.perform(post(new URI(POST_ENDPOINT))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem("Date can not be future")))
                .andExpect(jsonPath("$.errors", hasItem("number of Likes should be greater or equal zero")))
                .andExpect(jsonPath("$.errors", hasItem("Text should be b-n 1 and 300 characters")));

    }

    @Test
    void validationCheckWhenCreatingWithBlankValues() throws Exception {
        Post post = new Post("", "", null, null);
        String json = mapper.writeValueAsString(post);
        mockMvc.perform(post(new URI(POST_ENDPOINT))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(5)))
                .andExpect(jsonPath("$.errors", hasItem("Please provide a date")))
                .andExpect(jsonPath("$.errors", hasItem("number of Likes is mandatory")))
                .andExpect(jsonPath("$.errors", hasItem("Post text is mandatory")))
                .andExpect(jsonPath("$.errors", hasItem("Blog name is mandatory")))
                .andExpect(jsonPath("$.errors", hasItem("Blog name should be b-n 2 and 50 characters")));
    }


    Post createPost(Integer postId, String blogName, String text, Integer numberOfLikes, LocalDate date) {
        Post post = new Post(blogName, text, date, numberOfLikes);
        post.setPostId(postId);
        return post;
    }
}
