package com.saurabh.netflixapi.controller;
import com.saurabh.netflixapi.entity.Movie;
import com.saurabh.netflixapi.entity.Movies;
import com.saurabh.netflixapi.model.ImdbMovie;
import com.saurabh.netflixapi.model.OmdbSearchResults;
import com.saurabh.netflixapi.service.MovieService;
import com.saurabh.netflixapi.service.OmdbAPIService;

import com.saurabh.netflixapi.model.Node;
import com.saurabh.netflixapi.service.JSoupService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1")
public class MainController {



    @Autowired
    private final JSoupService jSoupService;

    private final OmdbAPIService omdbAPIService;
    private final MovieService movieService;
    private Map<String, String> myMap;

    public MainController(JSoupService jSoupService, OmdbAPIService omdbAPIService, MovieService movieService) {
        this.jSoupService = jSoupService;
        this.omdbAPIService = omdbAPIService;
        this.movieService = movieService;
    }


    @GetMapping("/anchors")
    public List<Element> getAnchorTags() throws IOException {
        String url = "https://sv3.hivamovie.com/new/Movie/";
        return jSoupService.getAnchorTags(url);
    }


    @GetMapping("/getFolderLinksFromALink")
    public List<String> getFolderLinksFromALink() throws IOException {
        String url = "https://sv3.hivamovie.com/new/Movie/";
        return jSoupService.getFolderLinksFromALink(url);
    }



    @GetMapping("/saveAllMoviesFromALink")
    public ResponseEntity<List<Movie>> getListOfNodesFromOneLink() throws IOException {

        List<Node> res = jSoupService.getListOfNodesFromLink();

        List<Movie> movieList = new ArrayList<>();
        for (Node node : res) {
            if (!node.getMovieName().isEmpty()) {

                Movie movie = omdbAPIService.getMovieByTitle(node);


                // why would imdb Id be nulgil? => maybe we don't have info for but that should be handled before inside the getMovieByTitle()
                if (movie.getImdbId() != null) {
                    movieList.add(movie);
                } else {
                    log.error("This node has some issue. Debug node to find out. New format hai boss!!");
                }
            }
        }

        return ResponseEntity.ok(movieList);

    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> res = movieService.getAllMovies();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/getAllFromMovies")
    public ResponseEntity<List<Movies>> getAll() {
        List<Movies> res = movieService.getAllMoviesFromMovies();
        return ResponseEntity.ok(res);
    }

    // create a get mapping to get movies with Resolution of 1080p
    @GetMapping("/movies/1080p")
    public ResponseEntity<List<Movie>> getMoviesWith1080p() {
        List<Movie> res = movieService.getMoviesWith1080p();
        return ResponseEntity.ok(res);
    }



        @GetMapping("/movies/{id}")
    public ImdbMovie getMovieById(@PathVariable String id) {

        return omdbAPIService.getMovieById(id);

    }

    @GetMapping("/cache")
    public String getFromCache() {
        return  myMap.get("Hello");
    }


    // TODO: use this API to somehow provide realtime search
    @GetMapping("/movies/search/{query}")
    public OmdbSearchResults getMoviesByQuery(@PathVariable String query) {

        return omdbAPIService.getMoviesByQuery(query);

    }

//
//    @GetMapping("/test")
//    public String getMovieByTitle() {
//
//        try {
//            URL url = new URL("https://jsonmock.hackerrank.com/api/tvseries");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Accept", "application/json");
//
//            if (connection.getResponseCode() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
//            }
//
//            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
//            String output;
//            StringBuilder response = new StringBuilder();
//            while ((output = br.readLine()) != null) {
//                response.append(output);
//            }
//            connection.disconnect();
//
//            JSONParser parser = new JSONParser();
//            JSONObject json =(JSONObject) parser.parse(response.toString());
//
//            for (Series series : JSONObject.get("data")) {
//                if (series.getGenre().equalsIgnoreCase(genre)) {
//                    if (series.getImdbRating() > highestRating) {
//                        highestRating = series.getImdbRating();
//                        bestSeriesName = series.getName();
//                    } else if (series.getImdbRating() == highestRating) {
//                        bestSeriesName = series.getName().compareTo(bestSeriesName) < 0 ? series.getName() : bestSeriesName;
//                    }
//                }
//            }
//
//            return bestSeriesName;
//
//            System.out.println("Parsed JSON using google/gson: " + json);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        return "OK";
//
//    }





    // Testing endpoint
    @GetMapping("/fun")
    public ResponseEntity<String> newendpoint() {

        String res = "saurabh and ronak!";
        return ResponseEntity.ok(res);

    }
}
