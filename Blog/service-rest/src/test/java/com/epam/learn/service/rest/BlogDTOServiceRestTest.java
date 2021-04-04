package com.epam.learn.service.rest;

import com.epam.learn.model.dto.BlogDTO;
import com.epam.learn.service.blog.BlogDtoService;
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
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class BlogDTOServiceRestTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(BlogDTOServiceRestTest.class);

    public static final String BLOG_DTO_URL="http://localhost:8090/blogs-dto";

    @Autowired
    BlogDtoService blogDtoService;


    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldGetAllBlogsWithMaxLikes() throws Exception {
        // mocked URI with stubbed return parameters
        LOGGER.debug("shouldGetAllBlogsWithMaxLikes()");
        mockServer.expect(ExpectedCount.once(),requestTo(new URI(BLOG_DTO_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.writeValueAsString(List.of(
                        createBlogDTO(0),
                        createBlogDTO(1))
                )));

        // call of mocked URI
        List<BlogDTO> allBlogsWithMaxLikes = blogDtoService.getAllBlogsWithMaxLikes();

        // some tests of correct behavior
        mockServer.verify();

        assertNotNull(allBlogsWithMaxLikes);
        assertTrue(allBlogsWithMaxLikes.size() > 0);



    }

    private BlogDTO createBlogDTO(Integer id){
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setBlogId(id);
        blogDTO.setBlogName("blog "+ id);
        return blogDTO;
    }

}