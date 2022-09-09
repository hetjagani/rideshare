package com.rideshare.ride.controller;

import com.rideshare.ride.model.Tag;
import com.rideshare.ride.service.ITagsService;
import com.rideshare.ride.webentity.DeleteSuccess;
import com.rideshare.ride.webentity.PaginatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/tags")
public class TagsController {
    @Autowired
    ITagsService tagsService;

    @GetMapping
    public ResponseEntity<PaginatedEntity<Tag>> searchTags(@RequestParam(required = false) String query, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception {
        try {
            PaginatedEntity<Tag> tags = tagsService.searchTags(query, page, limit);
            return ResponseEntity.ok(tags);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) throws Exception {
        try {
            Tag createdTag = tagsService.create(tag);
            return ResponseEntity.ok(createdTag);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<DeleteSuccess> createTag(@PathVariable Integer id) throws Exception {
        try {
            if (tagsService.delete(id)) {
                return ResponseEntity.ok(new DeleteSuccess(true));
            }
            return ResponseEntity.ok(new DeleteSuccess(false));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
