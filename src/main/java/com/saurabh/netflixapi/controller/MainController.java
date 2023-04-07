package com.saurabh.netflixapi.controller;
import com.saurabh.netflixapi.entity.Movie;
import com.saurabh.netflixapi.model.ImdbMovie;
import com.saurabh.netflixapi.model.OmdbSearchResults;
import com.saurabh.netflixapi.service.MovieService;
import com.saurabh.netflixapi.service.OmdbAPIService;

import com.saurabh.netflixapi.model.Node;
import com.saurabh.netflixapi.service.JSoupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(maxAge = 3600, allowedHeaders = "*" )
@RequestMapping(path = "/api/v1")
public class MainController {



    @Autowired
    private final JSoupService JSoupService;

    private final OmdbAPIService omdbAPIService;
    private final MovieService movieService;
    private Map<String, String> myMap;

    public MainController(JSoupService JSoupService, OmdbAPIService omdbAPIService, MovieService movieService) {
        this.JSoupService = JSoupService;
        this.omdbAPIService = omdbAPIService;
        this.movieService = movieService;
    }

    @GetMapping("/saveAllMoviesFromALink")
    public ResponseEntity<List<Movie>> getListOfNodesFromOneLink() {

        List<Node> res = JSoupService.getListOfNodesFromLink();

        List<Movie> movieList = new ArrayList<>();
        for (Node node : res) {
            if (node.getRawMovieName().contains(".mp4") && !node.getMovieName().isEmpty()) { // TODO: Add support for more media types

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


//    @GetMapping("/movies/bytitle/{title}")
//    public ImdbMovieEntity getMovieByTitle(@PathVariable String title) {
//
//        return omdbAPIService.getMovieByTitle(title);
//
//    }





    // Testing endpoint
    @GetMapping("/fun")
    public ResponseEntity<String> newendpoint() {

        String res = "saurabh and ronak!";
        return ResponseEntity.ok(res);

    }
}
