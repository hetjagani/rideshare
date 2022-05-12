package com.rideshare.userinfo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/hello")
@PreAuthorize("hasAuthority('ADMIN')")
public class TestController {

    @GetMapping
    public ResponseEntity<String> greeting() {
        return ResponseEntity.ok( "Hello World");
    }

}
