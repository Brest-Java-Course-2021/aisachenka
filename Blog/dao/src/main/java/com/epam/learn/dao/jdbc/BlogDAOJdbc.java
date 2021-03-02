package com.epam.learn.dao.jdbc;


import com.epam.learn.model.Blog;
import com.epam.learn.dao.BlogDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.*;


public class BlogDAOJdbc implements BlogDAO {

    public static final Logger LOGGER = LoggerFactory.getLogger(BlogDAOJdbc.class);

    public static final String SQL_FIND_ALL_QUERY = "SELECT * FROM BLOG AS B ORDER BY BLOG_NAME";
    public static final String SQL_FIND_BY_ID_QUERY = "SELECT * FROM BLOG AS B WHERE B.BLOG_ID = :BLOG_ID";
    public static final String SQL_INSERT_BLOG_QUERY = "INSERT INTO BLOG(BLOG_NAME) VALUES(:BLOG_NAME)";
    public static final String SQL_UPDATE_BLOG_QUERY = "UPDATE BLOG SET BLOG_NAME=:BLOG_NAME WHERE BLOG_ID = :BLOG_ID";
    public static final String SQL_DELETE_BLOG_QUERY = "DELETE FROM BLOG WHERE BLOG_ID = :BLOG_ID";
    public static final String SQL_COUNT_BLOG_NAME_QUERY = "SELECT COUNT(BLOG_NAME) FROM BLOG WHERE lower(BLOG_NAME)=lower(:BLOG_NAME)";
    public static final String SQL_COUNT_BLOG_ID_QUERY = "SELECT COUNT(BLOG_ID) FROM BLOG WHERE BLOG_ID=:BLOG_ID";

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    RowMapper<Blog> rowMapper = BeanPropertyRowMapper.newInstance(Blog.class);

    BlogDAOJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Blog> findAll() {
        LOGGER.debug("Find all Blogs");
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL_QUERY, rowMapper);
    }

    @Override
    public Optional<Blog> findById(Integer blogId) {
        LOGGER.debug("Find Blogs by Id {}", blogId);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("BLOG_ID", blogId);
        return Optional.ofNullable((Blog) namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_ID_QUERY, sqlParameterSource, rowMapper));
    }

    @Override
    public Integer create(Blog blog) {
        LOGGER.debug("Create blog {}", blog);

        if(!isBlogNameUnique(blog)) throw new IllegalArgumentException("Blog  with the same name already exists");

        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("BLOG_NAME", blog.getBlogName());
        namedParameterJdbcTemplate.update(SQL_INSERT_BLOG_QUERY, sqlParameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    private boolean isBlogNameUnique(Blog blog){
        return namedParameterJdbcTemplate.queryForObject(SQL_COUNT_BLOG_NAME_QUERY,
                new MapSqlParameterSource("BLOG_NAME", blog.getBlogName()), Integer.class ) == 0;
    }

    private boolean isBlogIdExists(Integer blogId){
        return namedParameterJdbcTemplate.queryForObject(SQL_COUNT_BLOG_ID_QUERY,
                new MapSqlParameterSource("BLOG_ID", blogId), Integer.class ) == 1;
    }

    @Override
    public Integer update(Blog blog) {
        LOGGER.debug("Update blog {}", blog);

        if(!isBlogNameUnique(blog)){
            LOGGER.warn("cannot update a field with a blog name that already exists {}", blog);
            throw new IllegalArgumentException("cannot update a field with a blog name that already exists");
        }
        if(!isBlogIdExists(blog.getBlogId())) {
            LOGGER.warn("this blog Id doesn't exists {}", blog);
            throw new IllegalArgumentException("this blog Id doesn't exists");
        }
        Map<String, Object> parametersForQuery = new HashMap<>();
        parametersForQuery.put("BLOG_NAME", blog.getBlogName());
        parametersForQuery.put("BLOG_ID", blog.getBlogId());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parametersForQuery);
        return namedParameterJdbcTemplate.update(SQL_UPDATE_BLOG_QUERY, sqlParameterSource);
    }

    @Override
    public Integer delete(Integer blogId) {
        LOGGER.debug("Delete blog by id {}", blogId);
        if(!isBlogIdExists(blogId)) throw new IllegalArgumentException("this blog Id doesn't exists");
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("BLOG_ID", blogId);
        return namedParameterJdbcTemplate.update(SQL_DELETE_BLOG_QUERY,sqlParameterSource);
    }
}
