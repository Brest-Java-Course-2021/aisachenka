package com.epam.learn.service.web_app;

import com.epam.learn.model.Blog;
import com.epam.learn.model.Post;
import com.epam.learn.service.blog.BlogDtoService;
import com.epam.learn.service.blog.BlogService;
import com.epam.learn.service.post.PostService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ExtendWith(SpringExtension.class)
public class PostsControllerTest {

    private final static String MAIN_URI = "http://localhost:8080/";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BlogsController blogsController;

    @Autowired
    PostsController postsController;

    @MockBean
    BlogService blogService;

    @MockBean
    PostService postService;

    @MockBean
    BlogDtoService blogDtoService;

    @Captor
    private ArgumentCaptor<Post> captor;

    @Test
    void shouldReturnViewWithAllPosts() throws Exception {
        Post p1 = createPost(1, "animals", RandomStringUtils.randomAlphabetic(250), 999, LocalDate.of(2018, 6, 6));
        Post p2 = createPost(2, "Deep Purple", RandomStringUtils.randomAlphabetic(250), 666, LocalDate.of(2020, 1, 1));


        when(postService.findAll()).thenReturn(
                List.of(p1, p2)
        );

        mockMvc.perform(MockMvcRequestBuilders.get(new URI(new StringBuilder(MAIN_URI).append("posts").toString())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("postId", is(p1.getPostId())),
                                hasProperty("blogName", is(p1.getBlogName())),
                                hasProperty("numberOfLikes", is(p1.getNumberOfLikes())),
                                hasProperty("localDate", is(p1.getLocalDate())),
                                hasProperty("text", is(p1.getText()))
                        )
                )))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("postId", is(p2.getPostId())),
                                hasProperty("blogName", is(p2.getBlogName())),
                                hasProperty("numberOfLikes", is(p2.getNumberOfLikes())),
                                hasProperty("localDate", is(p2.getLocalDate())),
                                hasProperty("text", is(p2.getText()))
                        )
                )));
    }

    @Test
    void shouldGetFormOfAddingPost() throws Exception {
        Blog b1 = createBlog(1, "TEST1");
        Blog b2 = createBlog(2, "TEST2");
        when(blogService.findAll()).thenReturn(List.of(b1, b2));

        mockMvc.perform(MockMvcRequestBuilders.get(new URI("/post/add")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("newPost"))
                .andExpect(model().attribute("blogs", hasItem(
                        allOf(
                                hasProperty("blogName", is(b1.getBlogName()))
                        )
                )))
                .andExpect(model().attribute("blogs", hasItem(
                        allOf(
                                hasProperty("blogName", is(b2.getBlogName()))
                        )
                )))
                .andExpect(model().attribute("postModel", hasProperty("postId", isEmptyOrNullString())))
                .andExpect(model().attribute("postModel", hasProperty("blogName", isEmptyOrNullString())))
                .andExpect(model().attribute("postModel", hasProperty("text", isEmptyOrNullString())))
                .andExpect(model().attribute("postModel", hasProperty("localDate", isEmptyOrNullString())))
                .andExpect(model().attribute("postModel", hasProperty("numberOfLikes", isEmptyOrNullString())));
    }

    @Test
    void shouldAddPost() throws Exception {
        Post p1 = createPost(null, "kitty", "blah blah", 333, LocalDate.of(2020, 5, 5));

        mockMvc.perform(MockMvcRequestBuilders.post("/post/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("postId", "")
                .param("blogName", p1.getBlogName())
                .param("text", p1.getText())
                .param("localDate", p1.getLocalDate().toString())
                .param("numberOfLikes", String.valueOf(p1.getNumberOfLikes())))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/posts"))
                .andExpect(redirectedUrl("/posts"));

        verify(postService).create(captor.capture());
        assertEquals(p1,captor.getValue());
    }

    @Test
    void shouldGetFormOfEditingPost() throws Exception {
        Blog b1 = createBlog(1, "TEST1");
        Blog b2 = createBlog(2, "TEST2");
        when(blogService.findAll()).thenReturn(List.of(b1, b2));

        Integer searchedId = 1;
        Post p1 = createPost(searchedId, b1.getBlogName(),"blah blah", 444, LocalDate.of(2018,12,31));
        when(postService.findById(searchedId)).thenReturn(Optional.of(p1));

        mockMvc.perform(MockMvcRequestBuilders.get(new URI("/post/" + searchedId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attribute("blogs", hasItem(
                        allOf(
                                hasProperty("blogName", is(b1.getBlogName()))
                        )
                )))
                .andExpect(model().attribute("blogs", hasItem(
                        allOf(
                                hasProperty("blogName", is(b2.getBlogName()))
                        )
                )))
                .andExpect(model().attribute("postModel", hasProperty("postId", is(p1.getPostId()))))
                .andExpect(model().attribute("postModel", hasProperty("blogName", is(p1.getBlogName()))))
                .andExpect(model().attribute("postModel", hasProperty("text", is(p1.getText()))))
                .andExpect(model().attribute("postModel", hasProperty("localDate", is(p1.getLocalDate()))))
                .andExpect(model().attribute("postModel", hasProperty("numberOfLikes", is(p1.getNumberOfLikes()))));
    }

    @Test
    void shouldUpdatePost() throws Exception {
        Post p1 = createPost(1, "kitty", "blah blah", 333, LocalDate.of(2020, 5, 5));

        mockMvc.perform(MockMvcRequestBuilders.post("/post/"+p1.getPostId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("postId", p1.getPostId().toString())
                .param("blogName", p1.getBlogName())
                .param("text", p1.getText())
                .param("localDate", p1.getLocalDate().toString())
                .param("numberOfLikes", String.valueOf(p1.getNumberOfLikes())))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/posts"))
                .andExpect(redirectedUrl("/posts"));

        verify(postService).update(captor.capture());
        assertEquals(p1,captor.getValue());
    }

    @Test
    void shouldDeletePost() throws Exception {
        Integer idOfPostThatShouldBeDeleted = 1;
        when(postService.delete(idOfPostThatShouldBeDeleted)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/post/"+idOfPostThatShouldBeDeleted+"/delete"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/posts"))
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void shouldSearchPosts() throws Exception {
        Post p1 = createPost(1, "animals", RandomStringUtils.randomAlphabetic(250), 999, LocalDate.of(2018, 6, 6));
        Post p2 = createPost(2, "Deep Purple", RandomStringUtils.randomAlphabetic(250), 666, LocalDate.of(2020, 1, 1));


        when(postService.searchByTwoDates(LocalDate.of(2018, 6, 6),LocalDate.of(2020, 1, 1))).thenReturn(
                List.of(p1, p2)
        );

        mockMvc.perform(MockMvcRequestBuilders.get(new URI("/search"))
                .param("dateBefore", LocalDate.of(2018, 6, 6).toString())
                .param("dateAfter", LocalDate.of(2020, 1, 1).toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("postId", is(p1.getPostId())),
                                hasProperty("blogName", is(p1.getBlogName())),
                                hasProperty("numberOfLikes", is(p1.getNumberOfLikes())),
                                hasProperty("localDate", is(p1.getLocalDate())),
                                hasProperty("text", is(p1.getText()))
                        )
                )))
                .andExpect(model().attribute("posts", hasItem(
                        allOf(
                                hasProperty("postId", is(p2.getPostId())),
                                hasProperty("blogName", is(p2.getBlogName())),
                                hasProperty("numberOfLikes", is(p2.getNumberOfLikes())),
                                hasProperty("localDate", is(p2.getLocalDate())),
                                hasProperty("text", is(p2.getText()))
                        )
                )));
    }


    Post createPost(Integer postId, String blogName, String text, Integer numberOfLikes, LocalDate date) {
        Post post = new Post(blogName, text, date, numberOfLikes);
        post.setPostId(postId);
        return post;
    }

    Blog createBlog(Integer blogId, String blogName) {
        Blog blog = new Blog(blogName);
        blog.setBlogId(blogId);
        return blog;
    }
}
