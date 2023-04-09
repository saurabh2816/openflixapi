package com.saurabh.netflixapi.service;

import com.saurabh.netflixapi.entity.Movie;
import com.saurabh.netflixapi.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return this.movieRepository.findAll();
    }


    public List<Movie> getMoviesWith1080p() {
        return this.movieRepository.findByResolution("1080p");
    }
}
