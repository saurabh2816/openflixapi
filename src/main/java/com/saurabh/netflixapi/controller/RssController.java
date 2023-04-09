package com.saurabh.netflixapi.controller;

import com.saurabh.netflixapi.service.RssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1/rss")
public class RssController {

    @Autowired
    private RssService rssService;

    // do constructor injection of rssService


    @GetMapping("/top-stories")
    public List<String> getTopStories() throws IOException {
        return rssService.getTopStories();
    }

}
