package com.saurabh.netflixapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
//@CrossOrigin(maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1")
public class MainController {

    @GetMapping(value = "/try")
    public ResponseEntity<String> getSomething() {
        return ResponseEntity.ok("saurabh");
    }
}
