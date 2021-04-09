package com.epam.learn.service.web_app;

import com.epam.learn.model.Blog;
import com.epam.learn.model.dto.BlogDTO;
import com.epam.learn.service.blog.BlogDtoService;
import com.epam.learn.service.blog.BlogService;
import com.epam.learn.service.post.PostService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class BlogsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BlogService blogService;

    @MockBean
    PostService postService;

    @MockBean
    BlogDtoService blogDtoService;

    @Captor
    private ArgumentCaptor<Blog> captor;



    @Test
    void shouldReturnAllBlogs() throws Exception {

        BlogDTO b1 = new BlogDTO(1, "test1", 123);
        BlogDTO b2 = new BlogDTO(2, "test2", 123);
        BlogDTO b3 = new BlogDTO(3, "test3", null);

        when(blogDtoService.getAllBlogsWithMaxLikes()).thenReturn(List.of(
                b1, b2, b3
        ));
        mockMvc.perform(get("/blogs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("blogs"))
                .andExpect(model().attribute("blogs", hasItem(
                        allOf(
                                hasProperty("blogId", is(b1.getBlogId())),
                                hasProperty("blogName", is(b1.getBlogName())),
                                hasProperty("maxNumberOfLikes", is(b1.getMaxNumberOfLikes()))
                        )))
                ).andExpect(model().attribute("blogs", hasItem(
                allOf(
                        hasProperty("blogId", is(b2.getBlogId())),
                        hasProperty("blogName", is(b2.getBlogName())),
                        hasProperty("maxNumberOfLikes", is(b2.getMaxNumberOfLikes()))
                )
        )))
                .andExpect(model().attribute("blogs", hasItem(
                        allOf(
                                hasProperty("blogId", is(b3.getBlogId())),
                                hasProperty("blogName", is(b3.getBlogName())),
                                hasProperty("maxNumberOfLikes", isEmptyOrNullString()))
                        )
                ))
        ;
    }

    @Test
    void shouldReturnEditFormWithValues() throws Exception {
        Blog b1 = new Blog("test1");
        b1.setBlogId(1);
        when(blogService.findById(1)).thenReturn(Optional.of(b1));

        mockMvc.perform(get("/blog/1"))
                .andDo(print())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog"))
                .andExpect(model().attribute("blog", hasProperty("blogId", is(b1.getBlogId()))))
                .andExpect(model().attribute("blog", hasProperty("blogName", is(b1.getBlogName()))));
    }

    @Test
    void shouldGetFormOfaddBlog() throws Exception {
        mockMvc.perform(get("/blog/add"))
                .andDo(print())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(view().name("newBlog"))
                .andExpect(model().attribute("blog", hasProperty("blogId", isEmptyOrNullString())))
                .andExpect(model().attribute("blog", hasProperty("blogName", isEmptyOrNullString())));

        verifyNoInteractions(blogDtoService, blogService);
    }

    @Test
    void shouldDeleteBlog() throws Exception {

        mockMvc.perform(get("/blog/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/blogs"))
                .andExpect(redirectedUrl("/blogs"));
    }

    @Test
    void shouldAddBlog() throws Exception {
        String blogName = RandomStringUtils.randomAlphabetic(50);

        mockMvc.perform(post("/blog/add").
                contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("blogName", blogName))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/blogs"))
                .andExpect(redirectedUrl("/blogs"));

        verify(blogService).create(captor.capture());

        Blog blog = captor.getValue();

        assertEquals(blogName, blog.getBlogName());
    }

    @Test
    void editBlog() throws Exception {
        String blogName = RandomStringUtils.randomAlphabetic(50);

        mockMvc.perform(post("/blog/1").
                contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("blogName", blogName))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/blogs"))
                .andExpect(redirectedUrl("/blogs"));

        verify(blogService).update(captor.capture());

        Blog blog = captor.getValue();

        assertEquals(blogName, blog.getBlogName());

    }


}