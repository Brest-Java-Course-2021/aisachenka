package com.epam.learn.service.rest_app;

import com.epam.learn.model.Blog;
import com.epam.learn.service.rest_app.exception.ControllerAdvisor;
import com.epam.learn.service.rest_app.exception.ErrorResponse;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        Integer id = 1;
        MockHttpServletResponse response = mockMvc.perform(delete(BLOGS_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), new TypeReference<ErrorResponse>() {
        });

        assertNotNull(errorResponse);
        assertEquals(errorResponse.getErrors().get(0),"Can't delete Blog because it has dependencies");
    }

    @Test
    void shouldReturnErrorWhenUpdateWithTooLongValue() throws Exception{

        Blog blog = new Blog();
        blog.setBlogName(RandomStringUtils.randomAlphabetic(53));
        blog.setBlogId(1);
        MockHttpServletResponse response = mockMvc.perform(put(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blog))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), new TypeReference<ErrorResponse>() {
        });

        assertNotNull(errorResponse);
        assertEquals(errorResponse.getErrors().get(0),"Blog name should be b-n 2 and 50 characters");
    }

    @Test
    void shouldReturnErrorWhenUpdateWithBlankValue() throws Exception{
        Blog blog = new Blog();
        blog.setBlogName("");
        blog.setBlogId(1);
        MockHttpServletResponse response = mockMvc.perform(put(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blog))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), new TypeReference<ErrorResponse>() {
        });

        assertNotNull(errorResponse);
        assertEquals(errorResponse.getErrors().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList()),
                List.of(
                        "Blog name is mandatory",
                        "Blog name should be b-n 2 and 50 characters")
        );
    }


    @Test
    void shouldReturnErrorWhenCreateWithTooLongValue() throws Exception{

        Blog blog = new Blog();
        blog.setBlogName(RandomStringUtils.randomAlphabetic(53));
        blog.setBlogId(1);
        MockHttpServletResponse response = mockMvc.perform(post(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blog))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), new TypeReference<ErrorResponse>() {
        });

        assertNotNull(errorResponse);
        assertEquals(errorResponse.getErrors().get(0),"Blog name should be b-n 2 and 50 characters");
    }
    @Test
    void shouldReturnErrorWhenCreateWithBlankValue() throws Exception{

        Blog blog = new Blog();
        blog.setBlogName("");
        blog.setBlogId(1);
        MockHttpServletResponse response = mockMvc.perform(post(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blog))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), new TypeReference<ErrorResponse>() {
        });

        assertNotNull(errorResponse);
        assertEquals(errorResponse.getErrors().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList()),
                List.of(
                        "Blog name is mandatory",
                        "Blog name should be b-n 2 and 50 characters")
        );
    }

    @Test
    void shouldReturnErrorWhenUpdateWithNonexistentId() throws Exception{

        Blog blog = new Blog();
        blog.setBlogName(RandomStringUtils.randomAlphabetic(50));
        blog.setBlogId(99999);
        MockHttpServletResponse response = mockMvc.perform(put(BLOGS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(blog))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertNotNull(response);
    }


}
