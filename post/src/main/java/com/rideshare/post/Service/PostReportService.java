package com.rideshare.post.Service;

import com.rideshare.post.mapper.PostReportMapper;
import com.rideshare.post.model.Post;
import com.rideshare.post.model.PostReport;
import com.rideshare.post.webentity.PostReportDetails;
import com.rideshare.post.webentity.PostReportRequest;
import com.rideshare.post.webentity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostReportService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PostService postService;

    @Value("${app.userinfo.url}")
    private String userInfoUrl;

    public PostReportDetails getReportById(Integer id, String token) throws Exception{
        try{
            String getReportQuery = "SELECT * FROM \"post\".\"reported_post\" WHERE id=" + id;
            PostReport postReport = jdbcTemplate.queryForObject(getReportQuery, new PostReportMapper());
            Post post =  postService.getPostById(postReport.getPostId(), token);

            String requestURL = userInfoUrl + "/users/" + postReport.getUserId();

            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", token);

            HttpEntity request = new HttpEntity(header);

            ResponseEntity<User> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, User.class);

            User user = response.getBody();
            PostReportDetails postReportDetails = new PostReportDetails(postReport);
            postReportDetails.setUser(user);
            postReportDetails.setPost(post);
            return postReportDetails;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public PostReport create(PostReportRequest postReport, String token) throws Exception{
        try {
            String validatePostQuery = "SELECT COUNT(*) FROM \"post\".\"post\" WHERE id="+ postReport.getPostId();
            int count = jdbcTemplate.queryForObject(validatePostQuery, Integer.class);
            if(count != 1){
                throw new Exception("Post does not exists");
            }

            String createReportQuery = "INSERT INTO \"post\".\"reported_post\" (post_id, user_id, reason, description) VALUES (?,?,?,?) RETURNING id";
            Integer id = jdbcTemplate.queryForObject(createReportQuery, Integer.class, postReport.getPostId(), postReport.getUserId(), postReport.getReason(), postReport.getDescription());
            PostReport createdPostReport = getReportById(id, token);
            return createdPostReport;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public PostReport update(PostReportRequest postReport, String token, Integer id) throws Exception {
        try {
            String validatePostQuery = "SELECT COUNT(*) FROM \"post\".\"post\" WHERE id=" + postReport.getPostId();
            int count = jdbcTemplate.queryForObject(validatePostQuery, Integer.class);
            if (count != 1) {
                throw new Exception("Post does not exists");
            }

            String updateReportQuery = "UPDATE \"post\".\"reported_post\" SET reason=?, description=? WHERE id=? RETURNING id";
            Integer newId = jdbcTemplate.queryForObject(updateReportQuery, Integer.class, postReport.getReason(), postReport.getDescription(), id);
            PostReport updatePostReport = getReportById(newId, token);
            return updatePostReport;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Boolean delete(Integer id, Integer userId, String token) throws Exception{
        try{
            PostReport fetchedPostReport = getReportById(id, token);
            if(fetchedPostReport.getUserId() != userId){
                throw new Exception("Cannot delete post of different user");
            }
            String deletePostQuery = "DELETE FROM \"post\".\"reported_post\" WHERE id=" + id;
            Integer affectedRows = jdbcTemplate.update(deletePostQuery);

            return affectedRows != 0;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
