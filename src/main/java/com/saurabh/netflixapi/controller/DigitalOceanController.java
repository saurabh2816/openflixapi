package com.saurabh.netflixapi.controller;

import com.saurabh.netflixapi.DTO.S3FileDTO;
import com.saurabh.netflixapi.repository.S3FilesRepository;
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
    private S3FilesRepository s3FilesRepository;

    @Autowired
    private DigitalOceanSpacesService digitalOceanSpacesService;

    @GetMapping("/files")
    public List<S3FileDTO> listFiles() {
        return digitalOceanSpacesService.listFilesInFolder("Movies");
    }

    /* DITCHED THE PLAN TO USE S3FILES TABLE*/

//    @GetMapping("/saveVisitedFiles")
//    public List<S3Files> saveVisitedFiles() {
//
//        // get files from S3
//        List<S3ObjectSummary> files = digitalOceanSpacesService.listFilesInFolder("Movies");
//        List<S3Files> res = new ArrayList<>();
//
//        files.forEach( file -> {
//            res.add(S3Files.builder()
//                            .fileName(file.getKey())
//                            .size(file.getSize())
//                            .lastModified(file.getLastModified())
//                            .size(file.getSize())
//                            .etag(file.getETag())
//                            .bucket(file.getBucketName())
//                            .visited(false)
//                            .build());
//        });
//
//        s3FilesRepository.saveAll(res);
//        return res;
//
//    }

}
