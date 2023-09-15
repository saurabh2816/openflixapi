package com.saurabh.netflixapi.service;

import com.saurabh.netflixapi.entity.Movie;
import com.saurabh.netflixapi.entity.Movies;
import com.saurabh.netflixapi.repository.MovieRepository;
import com.saurabh.netflixapi.repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MoviesRepository moviesRepository;

    public MovieService(MovieRepository movieRepository, MoviesRepository moviesRepository) {
        this.movieRepository = movieRepository;
        this.moviesRepository = moviesRepository;
    }

    public List<Movie> getAllMovies() {
        return this.movieRepository.findAll();
    }


    public List<Movie> getMoviesWith1080p() {
        return this.movieRepository.findByResolution("1080p");
    }

    public List<Movies> getAllMoviesFromMovies() {
        return this.moviesRepository.findAll();
    }
}
