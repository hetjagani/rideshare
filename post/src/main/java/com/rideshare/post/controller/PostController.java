package com.rideshare.post.controller;

import com.rideshare.post.Service.PostService;
import com.rideshare.post.model.Post;
import com.rideshare.post.security.UserPrincipal;
import com.rideshare.post.webentity.PostEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    @Autowired
    private PostService postService;
    private Logger logger = LogManager.getLogger(PostController.class);
    @GetMapping
    public ResponseEntity getPosts() {
        return ResponseEntity.ok("Getting all the posts");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer id) throws Exception {
        return ResponseEntity.ok(postService.getPostById(id));
    }
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostEntity postData, @AuthenticationPrincipal UserPrincipal userDetails) throws Exception{
        try{
            postData.setUserId(Integer.parseInt(userDetails.getId()));
            Post post = postService.create(postData);
            return ResponseEntity.ok(post);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
