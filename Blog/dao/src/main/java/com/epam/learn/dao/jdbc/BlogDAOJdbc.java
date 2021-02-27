package com.epam.learn.dao.jdbc;


import com.epam.learn.model.Blog;
import com.epam.learn.dao.BlogDAO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.*;

public class BlogDAOJdbc implements BlogDAO {

    public static final String SQL_FIND_ALL_QUERY = "SELECT * FROM BLOG AS B ORDER BY BLOG_NAME";
    public static final String SQL_FIND_BY_ID_QUERY = "SELECT * FROM BLOG AS B WHERE B.BLOG_ID = :BLOG_ID";
    public static final String SQL_INSERT_BLOG_QUERY = "INSERT INTO BLOG(BLOG_NAME) VALUES(:BLOG_NAME)";
    public static final String SQL_UPDATE_BLOG_QUERY = "UPDATE BLOG SET BLOG_NAME=:BLOG_NAME WHERE BLOG_ID = :BLOG_ID";
    public static final String SQL_DELETE_BLOG_QUERY = "DELETE FROM BLOG WHERE BLOG_ID = :BLOG_ID";

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    RowMapper<Blog> rowMapper = BeanPropertyRowMapper.newInstance(Blog.class);

    BlogDAOJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Blog> findAll() {
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL_QUERY, rowMapper);
    }

    @Override
    public Optional<Blog> findById(Integer blogId) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("BLOG_ID", blogId);
        return Optional.ofNullable((Blog) namedParameterJdbcTemplate.queryForObject(SQL_FIND_BY_ID_QUERY, sqlParameterSource, rowMapper));
    }

    @Override
    public Integer create(Blog blog) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("BLOG_NAME", blog.getBlogName());
        namedParameterJdbcTemplate.update(SQL_INSERT_BLOG_QUERY, sqlParameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Integer update(Blog blog) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> parametersForQuery = new HashMap<>();
        parametersForQuery.put("BLOG_NAME", blog.getBlogName());
        parametersForQuery.put("BLOG_ID", blog.getBlogId());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parametersForQuery);
        namedParameterJdbcTemplate.update(SQL_UPDATE_BLOG_QUERY, sqlParameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Integer delete(Integer blogId) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("BLOG_ID", blogId);
        return namedParameterJdbcTemplate.update(SQL_DELETE_BLOG_QUERY,sqlParameterSource);
    }
}
