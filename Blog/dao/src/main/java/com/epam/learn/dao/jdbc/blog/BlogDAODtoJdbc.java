package com.epam.learn.dao.jdbc.blog;

import com.epam.learn.dao.blog.BlogDtoDAO;
import com.epam.learn.model.dto.BlogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BlogDAODtoJdbc implements BlogDtoDAO {
    public static final Logger LOGGER = LoggerFactory.getLogger(BlogDAOJdbc.class);

    @Value("${blogDTO.selectWithMaxLikes}")
    private String SQL_FIND_ALL_WITH_LIKES;

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    BlogDAODtoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public List<BlogDTO> getAllBlogsWithMaxLikes() {
        LOGGER.debug("getAllBlogsWithMaxLikes()");
        return namedParameterJdbcTemplate.query(SQL_FIND_ALL_WITH_LIKES, BeanPropertyRowMapper.newInstance(BlogDTO.class));

    }
}
