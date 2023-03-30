package com.saurabh.netflixapi.service;

import com.saurabh.netflixapi.entity.Movie;
import com.saurabh.netflixapi.repository.MovieRepository;
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


}