package com.rideshare.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/payments")
public class PaymentController {

    @GetMapping(path = "/{userId}")
    public ResponseEntity getPaymentsForUser(@PathVariable String userId) {

        return ResponseEntity.ok("Getting Payments for userId: "+userId);

    }

}
