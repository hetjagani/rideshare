package com.rideshare.post.service;

import com.rideshare.post.facade.UserInfoFacade;
import com.rideshare.post.mapper.PostImageMapper;
import com.rideshare.post.mapper.PostMapper;
import com.rideshare.post.model.*;
import com.rideshare.post.util.Pagination;
import com.rideshare.post.webentity.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PostService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserInfoFacade userInfoFacade;

    @Value("${app.ride.url}")
    private String rideUrl;

    @Value("${app.rating.url}")
    private String ratingUrl;

    public PaginatedEntity<Post> getPaginatedPosts(String token,
                                                   Integer page,
                                                   Integer limit,
                                                   Integer userId) throws Exception{
        try {
            Integer offset = Pagination.getOffset(page, limit);

            String getAllPostsQuery = "SELECT id FROM \"post\".\"post\"";
            if(userId != null){
                getAllPostsQuery += " WHERE user_id=" + userId;
            }

            getAllPostsQuery += " ORDER BY created_at DESC";

            if(limit != null && page != null){
                getAllPostsQuery += " LIMIT " + limit + " OFFSET " + offset;
            }
            List<Integer> postIds = jdbcTemplate.queryForList(getAllPostsQuery, Integer.class);
            List<Post> posts = new ArrayList<>();

            Map<Integer, UserInfo> userInfoMap = userInfoFacade.getAllUsers(token);
            log.info(userInfoMap.toString());

            for (Integer n : postIds) {
                posts.add(getPostById(n, token, userInfoMap));
            }

            return new PaginatedEntity<>(posts, page, limit);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Post getPostById(Integer id, String token, Map<Integer, UserInfo> userInfoMap) throws Exception{
        try{
            String getPostQuery = "SELECT * FROM \"post\".\"post\" WHERE id = " + id;
            Post post =  jdbcTemplate.queryForObject(getPostQuery, new PostMapper());

            String getPostImagesQuery = "SELECT * FROM \"post\".\"image\" WHERE post_id = " + id;
            List<PostImage> postImages = jdbcTemplate.query(getPostImagesQuery, new PostImageMapper());
            post.setImageList(postImages);

            post.setUser(userInfoMap.get(post.getUserId()));

            // Adding ride information if Post is for a RIDE
            if(PostType.RIDE.equals(post.getType())) {
                PostRide ridePost = new PostRide(post);
                String requestURL = rideUrl + "/rides/" + post.getRefId();

                HttpHeaders header = new HttpHeaders();
                header.add("Authorization", token);

                HttpEntity request = new HttpEntity(header);

                ResponseEntity<Ride> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, Ride.class);
                Ride rideInfo = response.getBody();
                ridePost.setRide(rideInfo);
                return ridePost;
            } else if(PostType.RATING.equals(post.getType())) { // Adding RATING information if Post is for a RATING
                PostRating ratingPost = new PostRating(post);
                String requestURL = ratingUrl + "/ratings/" + post.getRefId();

                HttpHeaders header = new HttpHeaders();
                header.add("Authorization", token);

                HttpEntity request = new HttpEntity(header);

                ResponseEntity<Rating> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, Rating.class);
                Rating rating = response.getBody();
                ratingPost.setRating(rating);
                return ratingPost;
            }
            return post;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Post create(PostEntity post, String token) throws Exception{
        try{
            String createPostQuery = "INSERT INTO \"post\".\"post\" (user_id, title, description, created_at, updated_at, type, ref_id, no_of_likes) VALUES (?,?,?,?,?,?,?,?) RETURNING id";
            Integer createdPostId = jdbcTemplate.queryForObject(createPostQuery, Integer.class, post.getUserId(), post.getTitle(), post.getDescription(), Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), post.getType(), post.getRefId(), 0);

            if(!post.getImageUrls().isEmpty()){
                for(String s: post.getImageUrls()){
                    String createImageQuery = "INSERT INTO \"post\".\"image\" (post_id, url) VALUES (?,?) RETURNING id";
                    Integer id = jdbcTemplate.queryForObject(createImageQuery, Integer.class, createdPostId, s);
                }
            }

            Map<Integer, UserInfo> userInfoMap = userInfoFacade.getAllUsers(token);

            Post retrievedPost = getPostById(createdPostId, token, userInfoMap);
            return retrievedPost;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Post update(PostEntity post, Integer id, String token) throws Exception{
        try{
            Map<Integer, UserInfo> userInfoMap = userInfoFacade.getAllUsers(token);

            Post fetchedPost = getPostById(id, token, userInfoMap);
            if(fetchedPost.getUserId() != post.getUserId()){
                throw new Exception("Cannot update post from different user");
            }
            String updatePostQuery = "UPDATE \"post\".\"post\" SET title = ?, description = ?, updated_at = ?, type = ?, ref_id = ?, no_of_likes = ? WHERE id=?";
            jdbcTemplate.update(updatePostQuery, post.getTitle(), post.getDescription(), Timestamp.from(Instant.now()), post.getType(), post.getRefId(), post.getNoOfLikes(), id);

            if(!post.getImageUrls().isEmpty()){
                String deleteImageQuery = "DELETE FROM \"post\".\"image\" WHERE post_id =" + fetchedPost.getId();
                jdbcTemplate.update(deleteImageQuery);

                for(String s: post.getImageUrls()){
                    String createImageQuery = "INSERT INTO \"post\".\"image\" (post_id, url) VALUES (?,?) RETURNING id";
                    Integer newId = jdbcTemplate.queryForObject(createImageQuery, Integer.class, id, s);
                }
            }


            Post retrievedPost = getPostById(id, token, userInfoMap);
            return retrievedPost;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Boolean delete(Integer id, Integer userId, String token) throws Exception{
        try{
            Map<Integer, UserInfo> userInfoMap = userInfoFacade.getAllUsers(token);
            Post fetchedPost = getPostById(id, token, userInfoMap);
            if(fetchedPost.getUserId() != userId){
                throw new Exception("Cannot delete post of different user");
            }
            String deletePostQuery = "DELETE FROM \"post\".\"post\" WHERE id =" + id;
            Integer affectedRows = jdbcTemplate.update(deletePostQuery);

            return affectedRows != 0;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
