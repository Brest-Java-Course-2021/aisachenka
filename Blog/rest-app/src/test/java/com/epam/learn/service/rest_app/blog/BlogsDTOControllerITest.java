package com.epam.learn.service.rest_app.blog;

import com.epam.learn.model.dto.BlogDTO;
import com.epam.learn.service.rest_app.BlogsDTOController;
import com.epam.learn.service.rest_app.exception.ControllerAdvisor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BlogsDTOControllerITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogsDTOControllerITest.class);

    private static final String BLOGS_DTO_ENDPOINT = "/blogs-dto";

    @Autowired
    BlogsDTOController blogsDTOController;

    @Autowired
    private ControllerAdvisor controllerAdvisor;


    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    MockBlogsDTOService blogsDTOService = new MockBlogsDTOService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(blogsDTOController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(controllerAdvisor)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllBlogsWithDTO() throws Exception {
        LOGGER.debug("shouldGetAllBlogsWithDTO()");
        List<BlogDTO> allBlogsWithMaxLikes = blogsDTOService.getAllBlogsWithMaxLikes();
        assertTrue(allBlogsWithMaxLikes.get(1).getBlogId()==2);
        assertTrue(allBlogsWithMaxLikes.get(1).getMaxNumberOfLikes()==4454);

        assertTrue(allBlogsWithMaxLikes.get(2).getBlogId()==1);
        assertTrue(allBlogsWithMaxLikes.get(2).getMaxNumberOfLikes()==3131);
    }


    class MockBlogsDTOService{
        public List<BlogDTO> getAllBlogsWithMaxLikes() throws Exception {
            LOGGER.debug("getAllBlogsWithMaxLikes");
            MockHttpServletResponse response = mockMvc.perform(get(BLOGS_DTO_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn().getResponse();

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<BlogDTO>>() {});
        }
    }
}
