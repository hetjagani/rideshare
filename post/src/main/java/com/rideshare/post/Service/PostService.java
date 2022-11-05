package com.rideshare.post.Service;

import com.rideshare.post.controller.PostController;
import com.rideshare.post.mapper.PostImageMapper;
import com.rideshare.post.mapper.PostMapper;
import com.rideshare.post.model.Post;
import com.rideshare.post.webentity.PostEntity;
import com.rideshare.post.webentity.PostImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class PostService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;
    private Logger logger = LogManager.getLogger(PostController.class);

    public Post getPostById(Integer id) throws Exception{
        try{
            String getPostQuery = "SELECT * FROM \"post\".\"post\" WHERE id = " + id;
            Post post =  jdbcTemplate.queryForObject(getPostQuery, new PostMapper());

            String getPostImagesQuery = "SELECT * FROM \"post\".\"image\" WHERE post_id = " + id;
            List<PostImage> postImages = jdbcTemplate.query(getPostImagesQuery, new PostImageMapper());
            post.setImageList(postImages);
            return post;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Post create(PostEntity post) throws Exception{
        try{
            String creatPostQuery = "INSERT INTO \"post\".\"post\" (user_id, title, description, created_at, updated_at, type, ride_id, no_of_likes) VALUES (?,?,?,?,?,?,?,?) RETURNING id";
            Integer createdPostId = jdbcTemplate.queryForObject(creatPostQuery, Integer.class, post.getUserId(), post.getTitle(), post.getDescription(), Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), post.getType(), post.getRideId(), post.getNoOfLikes());

            if(!post.getImageUrls().isEmpty()){
                for(String s: post.getImageUrls()){
                    String createImageQuery = "INSERT INTO \"post\".\"image\" (post_id, url) VALUES (?,?) RETURNING id";
                    Integer id = jdbcTemplate.queryForObject(createImageQuery, Integer.class, createdPostId, s);
                }
            }

            Post retrievedPost = getPostById(createdPostId);
            return retrievedPost;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
