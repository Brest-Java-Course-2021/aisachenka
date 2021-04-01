package com.epam.learn.dao.jdbc;


import com.epam.learn.dao.BlogDAO;
import com.epam.learn.dao.jdbc.exeption.ConstraintException;
import com.epam.learn.model.Blog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class BlogDAOJdbc implements BlogDAO {

    public static final Logger LOGGER = LoggerFactory.getLogger(BlogDAOJdbc.class);

    @Value("${blog.select}")
    private String SQL_FIND_ALL_QUERY;
    @Value("${blog.findById}")
    private String SQL_FIND_BY_ID_QUERY;
    @Value("${blog.create}")
    private String SQL_INSERT_BLOG_QUERY;
    @Value("${blog.update}")
    private String SQL_UPDATE_BLOG_QUERY;
    @Value("${blog.delete}")
    private String SQL_DELETE_BLOG_QUERY;
    @Value("${blog.count}")
    private String SQL_COUNT_BLOG_NAME_QUERY;


    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    RowMapper<Blog> rowMapper = BeanPropertyRowMapper.newInstance(Blog.class);

    BlogDAOJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
        List<Blog> results = namedParameterJdbcTemplate.query(SQL_FIND_BY_ID_QUERY, sqlParameterSource, rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
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


    @Override
    public Integer update(Blog blog) {
        LOGGER.debug("Update blog {}", blog);

        if(!isBlogNameUnique(blog)){
            LOGGER.warn("cannot update a field with a blog name that already exists {}", blog);
            throw new IllegalArgumentException("cannot update a field with a blog name that already exists");
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
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("BLOG_ID", blogId);
        Integer update;
        try {
            update = namedParameterJdbcTemplate.update(SQL_DELETE_BLOG_QUERY, sqlParameterSource);
        }catch (DataAccessException exception){
            LOGGER.warn("Can't delete Blog because it has dependencies id={} ",blogId);
            throw new ConstraintException("Can't delete Blog because it has dependencies");
        }

        return update;
    }
}
