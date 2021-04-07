package com.epam.learn.dao.jdbc.post;

import com.epam.learn.dao.blog.BlogDAO;
import com.epam.learn.dao.jdbc.exeption.SuchBlogNotExistsException;
import com.epam.learn.dao.post.PostDAO;
import com.epam.learn.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.time.LocalDate;
import java.util.*;

@Repository
public class PostDAOJdbc implements PostDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostDAOJdbc.class);

    @Value("${post.select}")
    private String select;

    @Value("${post.findById}")
    private String findById;

    @Value("${post.create}")
    private String create;

    @Value("${post.update}")
    private String update;

    @Value("${post.delete}")
    private String delete;

    @Value("${post.search}")
    private String searchByDate;

    private BlogDAO blogDAO;

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    RowMapper<Post> rowMapper = BeanPropertyRowMapper.newInstance(Post.class);

    @Autowired
    public PostDAOJdbc(DataSource dataSource, BlogDAO blogDAO){
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.blogDAO = blogDAO;
    }



    @Override
    public List<Post> findAll() {
        LOGGER.debug("findAll()");
        return namedParameterJdbcTemplate.query(select ,rowMapper);
    }

    @Override
    public Optional<Post> findById(Integer id) {
        LOGGER.debug("findById() {}",id);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("POST_ID", id);
        List<Post> results  = namedParameterJdbcTemplate.query(findById,sqlParameterSource,rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(results));
    }

    @Override
    public Integer create(Post post) {
        LOGGER.debug("create() {}",post);

        if(blogDAO.findByName(post.getBlogName()).isEmpty()){
            LOGGER.warn("Blog with such name doesn't exists {}", post);
            throw new SuchBlogNotExistsException("Blog with such name doesn't exists");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String,Object> parametrizedValues = new HashMap<>();
        parametrizedValues.put("BLOG_NAME", post.getBlogName());
        parametrizedValues.put("TEXT", post.getText());
        parametrizedValues.put("NUMBER_OF_LIKES", post.getNumberOfLikes());
        parametrizedValues.put("LOCAL_DATE", post.getLocalDate());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parametrizedValues);
        namedParameterJdbcTemplate.update(create, sqlParameterSource,keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Integer update(Post post) {
        LOGGER.debug("update() {}",post);

        if(blogDAO.findByName(post.getBlogName()).isEmpty()){
            LOGGER.warn("Blog with such name doesn't exists {}", post);
            throw new SuchBlogNotExistsException("Blog with such name doesn't exists");
        }

        Map<String,Object> parametrizedValues = new HashMap<>();
        parametrizedValues.put("BLOG_NAME", post.getBlogName());
        parametrizedValues.put("TEXT", post.getText());
        parametrizedValues.put("NUMBER_OF_LIKES", post.getNumberOfLikes());
        parametrizedValues.put("LOCAL_DATE", post.getLocalDate());
        parametrizedValues.put("POST_ID", post.getPostId());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parametrizedValues);
        return namedParameterJdbcTemplate.update(update, sqlParameterSource);
    }

    @Override
    public Integer delete(Integer id) {
        LOGGER.debug("delete() by id{} ", id);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("POST_ID",id);
        return namedParameterJdbcTemplate.update(delete,sqlParameterSource);
    }

    @Override
    public List<Post> searchByTwoDates(LocalDate dateBefore, LocalDate dateAfter) {
        LOGGER.debug("searchByTwoDates() before={} after={}", dateBefore, dateAfter);
        if(dateAfter.isBefore(dateBefore)) {
            LOGGER.warn("searchByTwoDates() throw IllegalArgumentException because Date After should be later than date before");
            throw new IllegalArgumentException("Date After should be later than date before");
        }
        Map<String,Object> parametrizedValues = new HashMap<>();
        parametrizedValues.put("DATE_BEFORE", dateBefore);
        parametrizedValues.put("DATE_AFTER", dateAfter);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(parametrizedValues);
        return namedParameterJdbcTemplate.query(searchByDate,sqlParameterSource,rowMapper);
    }

}
