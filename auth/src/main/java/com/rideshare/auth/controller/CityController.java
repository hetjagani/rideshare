package com.rideshare.auth.controller;

import com.rideshare.auth.model.City;
import com.rideshare.auth.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(path = "/cities")
public class CityController {

    @Autowired
    private ICityService cityService;


    @GetMapping(path = "hello")
    public ResponseEntity Hello() {
        return ResponseEntity.ok("Hello People World!!");
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityService.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<City> getCityByID(@PathVariable(value = "id") Long id) {
        System.out.println("ID: " + id);
        return ResponseEntity.ok(cityService.findById(id));
    }
}
