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
import com.saurabh.netflixapi.entity.Movie;
import com.saurabh.netflixapi.entity.Movies;
import com.saurabh.netflixapi.model.Node;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
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


    private final OmdbAPIService omdbAPIService;

    public DigitalOceanSpacesService(OmdbAPIService omdbAPIService) {
        this.omdbAPIService = omdbAPIService;
    }

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


        // get List<Node> from the summaries
        List<Node> resultList = new ArrayList<>();
        for(var summary: summaries) {
            addNodesFromLinks(summary.getEncodedUrl().toString(), resultList);
        }

        saveMovies(resultList);


        return summaries;
    }

    private void saveMovies(List<Node> nodes) {
        List<Movies> movieList = new ArrayList<>();
        for (Node node : nodes) {
            if (!node.getMovieName().isEmpty()) {

                Movies movie = omdbAPIService.saveToMoviesByTitle(node);


                // why would imdb Id be nulgil? => maybe we don't have info for but that should be handled before inside the getMovieByTitle()
                if (movie.getImdbId() != null) {
                    movieList.add(movie);
                    log.info("Movie: added to the list", movie);
                } else {
                    log.error("This node has some issue. Debug node to find out. New format hai boss!!");
                }
            }
        }

        System.out.print("ALl movies added");
    }


    private void addNodesFromLinks(String url, List<Node> resultList) {
            String decodedUrl = decodeUrl(url);
            Matcher matcher = getMatcher(decodedUrl);

            while (matcher.find()) {
                String rawFileName = matcher.group(0);
                String movieName = matcher.group(1).replace(".", " ");

                String rawStringAfterMovieName = matcher.group(2);
                String[] split = rawStringAfterMovieName.split("\\.");
                String movieYear = "";
                String movieResolution = "";
                String movieType = "";

                for (String s : split) {
                    if (s.matches("[0-9]{4}")) {
                        int year = Integer.parseInt(s);
                        if (year >= 1950 && year <= 2025) {
                            movieYear = s;
                        }
                    } else if (s.matches("[0-9]{3,4}p")) {
                        movieResolution = s;
                    } else if (split.length > 0) {
                        movieType = split[split.length - 1];
                    }
                }

                // ignore subtitles and trailer
                if( (movieType.equals("mp4") || movieType.equals("mkv")) && !rawFileName.contains("trailer") && !rawFileName.contains("Trailer") && !rawFileName.contains("sample") && !movieType.contains("sub") && !rawFileName.contains("srt")) {
                    addNewMovieNode(rawFileName, movieName, url, movieYear, movieResolution, movieType, resultList);
                }
            }

    }

    private String decodeUrl(String url) {
        String decodedUrl = "";
        try {
            decodedUrl = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedUrl;
    }

    private Matcher getMatcher(String decodedUrl) {
        String regex = "([\\.\\w']+?)(\\.[0-9]{4}\\..*)";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(decodedUrl);
    }

    private void addSubtitleToPreviousMovie(String movieName, Element link, List<Node> resultList) {
        if (!resultList.isEmpty() && resultList.get(resultList.size() - 1).getMovieName().equals(movieName)) {
            Node previousMovie = resultList.get(resultList.size() - 1);
            previousMovie.setStrLink(link.baseUri() + link.attr("href"));
        }
    }

    private void addNewMovieNode(String rawFileName, String movieName, String url, String movieYear, String movieResolution, String movieType, List<Node> resultList) {
        Node node = Node.builder()
                .rawMovieName(rawFileName)
                .movieName(movieName)
//                .text(link.text())
                .link(url)
                .resolution(movieResolution)
                .year(movieYear)
                .type(movieType)
                .build();
        resultList.add(node);
    }

}

