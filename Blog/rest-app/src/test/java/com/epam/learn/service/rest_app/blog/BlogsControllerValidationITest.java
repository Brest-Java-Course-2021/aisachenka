package com.epam.learn.service.rest_app.blog;

import com.epam.learn.model.Blog;
import com.epam.learn.service.rest_app.BlogsController;
import com.epam.learn.service.rest_app.exception.ControllerAdvisor;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BlogsControllerValidationITest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogsControllerValidationITest.class);
    private static final String BLOGS_ENDPOINT = "/blogs";

    MockMvc mockMvc;

    @Autowired
    BlogsController blogsController;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    private ControllerAdvisor controllerAdvisor;

    @BeforeEach
    void SetUp(){
        this.mockMvc = standaloneSetup(blogsController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(controllerAdvisor)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldReturnErrorWhenDeleteConstrainedField() throws Exception{
        LOGGER.debug("shouldReturnErrorWhenDeleteConstrainedField()");
        Integer id = 1;
        mockMvc.perform(delete(BLOGS_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Can't delete Blog because it has dependencies")));
    }

    @Test
    void shouldReturnErrorWhenUpdateWithTooLongValue() throws Exception{
        LOGGER.debug("shouldReturnErrorWhenUpdateWithTooLongValue()");
        Blog blog = new Blog();
        blog.setBlogName(RandomStringUtils.randomAlphabetic(53));
        blog.setBlogId(1);
        mockMvc.perform(put(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blog))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Blog name should be b-n 2 and 50 characters")));
    }

    @Test
    void shouldReturnErrorWhenUpdateWithBlankValue() throws Exception{
        LOGGER.debug("shouldReturnErrorWhenUpdateWithBlankValue()");
        Blog blog = new Blog();
        blog.setBlogName("");
        blog.setBlogId(1);
        mockMvc.perform(put(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blog))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors",hasSize(2)))
                .andExpect(jsonPath("$.errors",hasItem("Blog name is mandatory")))
                .andExpect(jsonPath("$.errors",hasItem("Blog name should be b-n 2 and 50 characters")));
    }


    @Test
    void shouldReturnErrorWhenCreateWithTooLongValue() throws Exception{
        LOGGER.debug("shouldReturnErrorWhenCreateWithTooLongValue()");
        Blog blog = new Blog();
        blog.setBlogName(RandomStringUtils.randomAlphabetic(53));
        blog.setBlogId(1);
        mockMvc.perform(post(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blog))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors",hasSize(1)))
                .andExpect(jsonPath("$.errors",hasItem("Blog name should be b-n 2 and 50 characters")));

    }
    @Test
    void shouldReturnErrorWhenCreateWithBlankValue() throws Exception{
        LOGGER.debug("shouldReturnErrorWhenCreateWithBlankValue()");
        Blog blog = new Blog();
        blog.setBlogName("");
        blog.setBlogId(1);
        mockMvc.perform(post(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blog))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors",hasSize(2)))
                .andExpect(jsonPath("$.errors",hasItem("Blog name is mandatory")))
                .andExpect(jsonPath("$.errors",hasItem("Blog name should be b-n 2 and 50 characters")));

    }

    @Test
    void shouldReturnErrorWhenUpdateWithNonexistentId() throws Exception{
        LOGGER.debug("shouldReturnErrorWhenUpdateWithNonexistentId()");
        Blog blog = new Blog();
        blog.setBlogName(RandomStringUtils.randomAlphabetic(50));
        blog.setBlogId(99999);
        mockMvc.perform(put(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blog))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Can't update Blog with such id")));
    }

    @Test
    void shouldReturnErrorWhenDeleteWithNonexistentId() throws Exception{
        LOGGER.debug("shouldReturnErrorWhenDeleteWithNonexistentId()");
        Integer id = 99999;
        mockMvc.perform(delete(BLOGS_ENDPOINT+"/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Can't delete Blog with such id")));
    }


}
