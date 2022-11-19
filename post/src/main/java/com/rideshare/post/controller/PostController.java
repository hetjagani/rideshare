package com.rideshare.post.controller;

import com.rideshare.post.Service.PostService;
import com.rideshare.post.model.Post;
import com.rideshare.post.security.UserPrincipal;
import com.rideshare.post.webentity.DeleteSuccess;
import com.rideshare.post.webentity.PaginatedEntity;
import com.rideshare.post.webentity.PostEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<PaginatedEntity<Post>> getPosts(@RequestHeader HttpHeaders headers,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer limit,
                                                          @RequestParam(required = false) Integer userId) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            return ResponseEntity.ok(postService.getPaginatedPosts(token, page, limit, userId));
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@RequestHeader HttpHeaders headers, @PathVariable Integer id) throws Exception {
        try {
            return ResponseEntity.ok(postService.getPostById(id, headers.get("Authorization").get(0)));
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestHeader HttpHeaders headers, @RequestBody PostEntity postData, @AuthenticationPrincipal UserPrincipal userDetails) throws Exception{
        try{
            String token = headers.get("Authorization").get(0);
            postData.setUserId(Integer.parseInt(userDetails.getId()));
            Post post = postService.create(postData, token);
            return ResponseEntity.ok(post);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePostById(@RequestHeader HttpHeaders headers, @RequestBody PostEntity postData, @PathVariable Integer id, @AuthenticationPrincipal UserPrincipal userDetails) throws Exception{
        try{
            postData.setUserId(Integer.parseInt(userDetails.getId()));
            String token = headers.get("Authorization").get(0);
            Post post = postService.update(postData, id, token);
            return ResponseEntity.ok(post);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<DeleteSuccess> deletePlace(@RequestHeader HttpHeaders headers, @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Integer postId) throws Exception {
        try {
            Integer userId = Integer.parseInt(userPrincipal.getId());
            String token = headers.get("Authorizarion").get(0);
            if(postService.delete(postId, userId, token)) {
                return ResponseEntity.ok(new DeleteSuccess(true));
            }
            return ResponseEntity.ok(new DeleteSuccess(false));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
