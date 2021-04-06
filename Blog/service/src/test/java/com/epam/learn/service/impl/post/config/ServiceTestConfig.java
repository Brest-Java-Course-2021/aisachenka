package com.epam.learn.service.impl.post.config;

import com.epam.learn.dao.blog.BlogDAO;
import com.epam.learn.dao.jdbc.blog.BlogDAOJdbc;
import com.epam.learn.dao.jdbc.post.PostDAOJdbc;
import com.epam.learn.dao.jdbc.post.PostDtoDAOJdbc;
import com.epam.learn.dao.post.PostDAO;
import com.epam.learn.dao.post.PostDtoDAO;
import com.epam.learn.service.impl.post.PostDtoServiceImpl;
import com.epam.learn.service.impl.post.PostServiceImpl;
import com.epam.learn.service.post.PostDtoService;
import com.epam.learn.service.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:dao.properties"})
public class ServiceTestConfig
{
    @Autowired
    DataSource dataSource;

    @Bean
    PostDAO postDAO(){
        return new PostDAOJdbc(dataSource, blogDAO());
    }

    @Bean
    PostDtoDAO postDtoDAO(){
        return new PostDtoDAOJdbc(dataSource);
    }

    @Bean
    BlogDAO blogDAO(){
        return new BlogDAOJdbc(dataSource);
    }

    @Bean
    PostService postService(){
        return new PostServiceImpl(postDAO());
    }

    @Bean
    PostDtoService postDtoService(){
        return new PostDtoServiceImpl(postDtoDAO());
    }
}
