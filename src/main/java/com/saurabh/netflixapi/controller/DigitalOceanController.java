package com.saurabh.netflixapi.controller;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.saurabh.netflixapi.service.DigitalOceanSpacesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/do")
public class DigitalOceanController {

    @Autowired
    private DigitalOceanSpacesService digitalOceanSpacesService;

    @GetMapping("/files")
    public List<S3ObjectSummary> listFiles() {
        return digitalOceanSpacesService.listFiles();
    }



}
