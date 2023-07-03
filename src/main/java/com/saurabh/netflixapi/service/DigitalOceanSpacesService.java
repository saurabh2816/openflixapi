package com.saurabh.netflixapi.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${digitalocean.bucketName}")
    private String bucketName;

    public List<S3ObjectSummary> listFiles() {
        BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();

        ObjectListing objectListing = s3Client.listObjects(bucketName);
        return objectListing.getObjectSummaries();
    }
//
//    DigitalOcean apiClient = new DigitalOceanClient("dop_v1_22b1161a10c0ae486327992dd9c07b5d9f109bae0551f7a6595522cc3612b340");
//
//    Droplets droplets;
//
//    {
//        try {
//            droplets = apiClient.getAvailableDroplets(1, 1);
//        } catch (DigitalOceanException e) {
//            throw new RuntimeException(e);
//        } catch (RequestUnsuccessfulException e) {
//            throw new RuntimeException(e);
//        }
//    }

}

