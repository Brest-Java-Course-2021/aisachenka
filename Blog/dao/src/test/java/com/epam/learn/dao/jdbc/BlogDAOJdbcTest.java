package com.epam.learn.dao.jdbc;

import com.epam.learn.dao.BlogDAO;
import com.epam.learn.model.Blog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-db.xml", "classpath*:test-dao.xml"})
public class BlogDAOJdbcTest {

    @Autowired
    BlogDAO blogDAO;

    @Test
    public void findAll() {
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


    @Test(expected = EmptyResultDataAccessException.class)
    public void findByIdExeptionalTest() {
        blogDAO.findById(999).get();
    }

    @Test
    public void create() {
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

    @Test(expected = IllegalArgumentException.class)
    public void createBlogWithSameName() {
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

        blogDAO.create(new Blog("hello"));
        blogDAO.create(new Blog("hello"));

        List<Blog> blogsAfterAdding = blogDAO.findAll();
        assertEquals(blogsAfterAdding.size(), blogs.size() + 1);
    }
    @Test(expected = IllegalArgumentException.class)
    public void createBlogWithSameNameDifferentCase() {
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

        blogDAO.create(new Blog("Hi"));
        blogDAO.create(new Blog("hi"));

    }


    @Test
    public void update() {
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


    @Test(expected = IllegalArgumentException.class)
    public void updateWithTheSameNameAsExists() {
        List<Blog> blogs = blogDAO.findAll();
        assertNotNull(blogs);
        assertTrue(blogs.size() > 0);
        for (Blog blog : blogs) {
            assertNotNull(blog.getBlogId());
            assertNotNull(blog.getBlogName());
        }

        Blog blog = blogs.get(0);
        blog.setBlogName(blogs.get(1).getBlogName());
        blogDAO.update(blog);
    }

    @Test
    public void delete() {
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


        Integer deletedCount = blogDAO.delete(1);
        List<Blog> blogsAfterDeleting = blogDAO.findAll();
        assertEquals(blogsAfterAdding.size(), blogsAfterDeleting.size() + 1);
        assertTrue(deletedCount == 1);

    }


}