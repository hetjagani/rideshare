package com.rideshare.post.controller;

import com.rideshare.post.service.PostReportService;
import com.rideshare.post.model.PostReport;
import com.rideshare.post.security.UserPrincipal;
import com.rideshare.post.webentity.DeleteSuccess;
import com.rideshare.post.webentity.PaginatedEntity;
import com.rideshare.post.webentity.PostReportDetails;
import com.rideshare.post.webentity.PostReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/posts/report")
public class PostReportController {
    @Autowired
    private PostReportService postReportService;

    @GetMapping
    public ResponseEntity<PaginatedEntity<PostReport>> getPostReports(@RequestHeader HttpHeaders headers,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer limit,
                                                          @RequestParam(required = false) Integer userId) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            return ResponseEntity.ok(postReportService.getPaginatedPostReports(token, page, limit, userId));
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @GetMapping("/{id}")
    public PostReportDetails getReport(@RequestHeader HttpHeaders headers, @PathVariable Integer id) throws Exception{
        String token = headers.get("Authorization").get(0);
        return postReportService.getReportById(id, token);
    }

    @PostMapping
    public PostReport createReport(@RequestHeader HttpHeaders headers, @AuthenticationPrincipal UserPrincipal userDetails, @RequestBody PostReportRequest postReport) throws Exception {
        String token = headers.get("Authorization").get(0);
        postReport.setUserId(Integer.parseInt(userDetails.getId()));
        return postReportService.create(postReport, token);
    }

    @PutMapping("/{id}")
    public PostReport updateReport(@RequestHeader HttpHeaders headers, @PathVariable Integer id,  @RequestBody PostReportRequest postReport) throws Exception {
        String token = headers.get("Authorization").get(0);
        return postReportService.update(postReport, token, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSuccess> delete(@PathVariable Integer id, @RequestHeader HttpHeaders headers, @AuthenticationPrincipal UserPrincipal userDetails) throws Exception {
        try {
            Integer userId = Integer.parseInt(userDetails.getId());
            String token = headers.get("Authorization").get(0);
            if(postReportService.delete(id, userId, token)) {
                return ResponseEntity.ok(new DeleteSuccess(true));
            }
            return ResponseEntity.ok(new DeleteSuccess(false));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
