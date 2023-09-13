package com.saurabh.netflixapi.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.saurabh.netflixapi.DTO.S3FileDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
@Service
public class DigitalOceanSpacesService {

    @Value("${digitalocean.accessKey}")
    private String accessKey;

    @Value("${digitalocean.secretKey}")
    private String secretKey;

    @Value("${digitalocean.region}")
    private String region;

    @Value("${digitalocean.endpoint}")
    private String endpoint;

    private AmazonS3 s3Client;

    @Value("${digitalocean.bucketName}")
    private String bucketName;

    @PostConstruct
    public void init() {
        try {
            // Initialize the S3 client once after bean properties are set
            BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
            s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(creds))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                    .build();

            // Set the bucket policy once during initialization
            setBucketPolicy();
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, log the error for further diagnosis
            // logger.error("Error initializing S3Service", e);
        }
    }

    private void setBucketPolicy() {
        String policyJson = "{"
                + "\"Version\":\"2012-10-17\","
                + "\"Statement\":["
                + "{"
                + "\"Effect\":\"Allow\","
                + "\"Principal\":\"*\","
                + "\"Action\":\"s3:GetObject\","
                + "\"Resource\":\"arn:aws:s3:::" + bucketName + "/Movies/*\""
                + "}"
                + "]"
                + "}";

        s3Client.setBucketPolicy(bucketName, policyJson);
    }

    public List<S3FileDTO> listFilesInFolder(String folderName) {
        List<S3FileDTO> summaries = new ArrayList<>();
        ObjectListing objectListing;

        String prefix = folderName.endsWith("/") ? folderName : folderName + "/";

        do {
            objectListing = s3Client.listObjects(new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withPrefix(prefix));

            for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {

                // Exclude the placeholder object for the folder
                if (!summary.getKey().equals(prefix)) {
                    URL fileUrl = s3Client.getUrl(bucketName, summary.getKey());
                    S3FileDTO s3FileDTO = new S3FileDTO(summary, fileUrl);
                    summaries.add(s3FileDTO);
                }
            }

            String nextMarker = objectListing.getNextMarker();
            objectListing.setMarker(nextMarker);
        } while (objectListing.isTruncated());


        return summaries;
    }


}

