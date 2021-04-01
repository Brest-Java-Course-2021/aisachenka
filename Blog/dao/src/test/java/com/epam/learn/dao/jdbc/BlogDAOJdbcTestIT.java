package com.epam.learn.dao.jdbc;

import com.epam.learn.dao.BlogDAO;
import com.epam.learn.dao.BlogDtoDAO;
import com.epam.learn.dao.jdbc.exeption.ConstraintException;
import com.epam.learn.model.Blog;
import com.epam.learn.testdb.SpringJdbcConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import({BlogDAOJdbc.class,BlogDAODtoJdbc.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BlogDAOJdbcTestIT {

    public static final Logger LOGGER = LoggerFactory.getLogger(BlogDAOJdbcTestIT.class);

    @Autowired
    BlogDAO blogDAO;

    @Autowired
    BlogDtoDAO blogDtoDAO;

    @Test
    public void findAll() {
        LOGGER.debug("findAll()");
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);

        // была проблема, что если ты в бд не назовешь поля как в модели, то тебе тупо вернет null даже ошибки не выдаст, что не может такого поля найти,
        // Spring, что сказать, рефлексия
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

    }

    @Test
    public void findById() {
        LOGGER.debug("findById()");
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }


        Integer blogId = blogs.get(2).getBlogId();
        Blog blog = blogDAO.findById(blogId).get();
        assertEquals(blogId, blog.getBlogId());
        assertEquals(blogs.get(2).getBlogName(), blog.getBlogName());
        assertEquals(blog, blogs.get(2));

    }


    @Test
    public void findByIdExeptionalTest() {
        LOGGER.debug("findByIdExeptionalTest()");
        assertFalse(blogDAO.findById(999).isPresent());
    }

    @Test
    public void create() {
        LOGGER.debug("create()");
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

        Blog blog = new Blog("Emptiness");
        blogDAO.create(blog);

        List<Blog> blogsAfterAdding = blogDAO.findAll();
        assertEquals(blogsAfterAdding.size(), blogs.size() + 1);
    }

    @Test
    public void createBlogWithSameName() {
        LOGGER.debug("createBlogWithSameName()");
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }


        assertThrows(IllegalArgumentException.class, () -> {
            blogDAO.create(new Blog("hello"));
            blogDAO.create(new Blog("hello"));
        });

    }

    @Test
    public void createBlogWithSameNameDifferentCase() {
        LOGGER.debug("createBlogWithSameNameDifferentCase()");
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

        assertThrows(IllegalArgumentException.class, () -> {
            blogDAO.create(new Blog("Hi"));
            blogDAO.create(new Blog("hi"));
        });
    }


    @Test
    public void update() {
        LOGGER.debug("update()");
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

        Blog blog = blogs.get(0);
        Integer blogId = blog.getBlogId();
        blog.setBlogName("Мои любимые юморески");
        blogDAO.update(blog);
        assertNotNull(blogDAO.findById(blogId));
        assertEquals("Мои любимые юморески", blogDAO.findById(blogId).get().getBlogName());
    }


    @Test
    public void updateWithTheSameNameAsExists() {
        LOGGER.debug("updateWithTheSameNameAsExists()");
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

        Blog blog = blogs.get(0);
        blog.setBlogName(blogs.get(1).getBlogName());

        assertThrows(IllegalArgumentException.class, () -> {
            blogDAO.update(blog);
        });

    }

    @Test
    public void delete() {
        LOGGER.debug("delete()");
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

        Blog blog = new Blog("I was a lonely estate");
        blogDAO.create(blog);
        List<Blog> blogsAfterAdding = blogDAO.findAll();
        assertEquals(blogsAfterAdding.size(), blogs.size() + 1);


        Integer deletedCount = blogDAO.delete(3);
        List<Blog> blogsAfterDeleting = blogDAO.findAll();
        assertEquals(blogsAfterAdding.size(), blogsAfterDeleting.size() + 1);
        assertTrue(deletedCount == 1);

    }

    @Test
    void shouldThrowExeptionOnDeleteOfConstrainedField(){
        LOGGER.debug("shouldThrowExeptionOnDeleteOfConstrainedField()");
        assertThrows(ConstraintException.class,()-> {
           blogDAO.delete(1);
        });


    }


}