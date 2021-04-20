package com.epam.learn.service.rest.post;

import com.epam.learn.model.Post;
import com.epam.learn.service.post.PostService;
import com.epam.learn.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
public class PostRestClientTest {

    private static final String POST_URL = "http://localhost:8090/posts";
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRestClientTest.class);


    @Autowired
    private PostService restClient;

    @Autowired
    private RestTemplate restTemplate;

    // to make jackson work with LocalDate https://github.com/FasterXML/jackson-modules-java8/tree/master/datetime
    private ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnAllPosts() throws Exception {
        LOGGER.debug("shouldReturnAllPosts()");
        //when
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(POST_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(List.of(
                                createPost(1, "animals", RandomStringUtils.randomAlphabetic(250), 666, LocalDate.of(2020, 1, 1)),
                                createPost(2, "kitty", RandomStringUtils.randomAlphabetic(250), 333, LocalDate.of(2018, 1, 1))
                        ))));

        List<Post> restClientAll = restClient.findAll();
        mockServer.verify();

        assertNotNull(restClientAll);
        assertTrue(restClientAll.size() > 0);

    }

    @Test
    void shouldReturnPostById() throws Exception {
        LOGGER.debug("shouldReturnPostById()");
        //when
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(POST_URL + "/" + 1)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(
                                Optional.of(createPost(1, "animals", RandomStringUtils.randomAlphabetic(250), 666, LocalDate.of(2020, 1, 1)))
                        )));

        Optional<Post> post = restClient.findById(1);
        mockServer.verify();

        assertTrue(post.isPresent());

    }

    @Test
    void shouldUpdatePost() throws Exception {
        LOGGER.debug("shouldReturnPostById()");
        //when
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(POST_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1")));

        Integer animals = restClient.update(createPost(1, "animals", RandomStringUtils.randomAlphabetic(250), 666, LocalDate.of(2020, 1, 1)));

        mockServer.verify();

        assertEquals((int) animals, 1);

    }

    @Test
    void shouldCreatePost() throws Exception {
        LOGGER.debug("shouldCreatePost()");
        //when
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(POST_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("5")));

        Integer animals = restClient.create(createPost(1, "animals", RandomStringUtils.randomAlphabetic(250), 666, LocalDate.of(2020, 1, 1)));

        mockServer.verify();

        assertEquals((int) animals, 5);

    }

    @Test
    void shouldDeletePost() throws Exception {
        LOGGER.debug("shouldDeletePost()");
        //when
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(POST_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString("1")));

        Integer animals = restClient.delete(id);

        mockServer.verify();

        assertNotNull(animals);
        assertEquals((int) animals, 1);

    }

    @Test
    void shouldFilterByDate() throws Exception {
        LOGGER.debug("shouldReturnAllPosts()");
        //when
        LocalDate dateBefore = LocalDate.of(2019, 1, 1);
        LocalDate dateAfter = LocalDate.of(2019, 5, 1);
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(new StringBuilder(POST_URL)
                .append("/search?dateBefore=").append(dateBefore.toString())
                .append("&dateAfter=").append(dateAfter).toString())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(List.of(
                                createPost(1, "animals", RandomStringUtils.randomAlphabetic(250), 666, LocalDate.of(2020, 1, 1)),
                                createPost(2, "kitty", RandomStringUtils.randomAlphabetic(250), 333, LocalDate.of(2018, 1, 1))
                        ))));

        List<Post> searchByTwoDates = restClient.searchByTwoDates(dateBefore, dateAfter);
        mockServer.verify();

        assertNotNull(searchByTwoDates);
        assertTrue(searchByTwoDates.size() > 0);

    }

    Post createPost(Integer postId, String blogName, String text, Integer numberOfLikes, LocalDate date) {
        Post post = new Post(blogName, text, date, numberOfLikes);
        post.setPostId(postId);
        return post;
    }
}
