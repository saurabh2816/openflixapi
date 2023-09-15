package com.saurabh.netflixapi.service;

import com.saurabh.netflixapi.entity.Movie;
import com.saurabh.netflixapi.entity.Movies;
import com.saurabh.netflixapi.exception.NetflixException;
import com.saurabh.netflixapi.model.ImdbMovie;
import com.saurabh.netflixapi.model.Node;
import com.saurabh.netflixapi.model.OmdbSearchResults;
import com.saurabh.netflixapi.repository.MovieRepository;
import com.saurabh.netflixapi.repository.MoviesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
public class OmdbAPIService {

    private MovieRepository movieRepository;
    private MoviesRepository moviesRepository;

    public OmdbAPIService(MovieRepository movieRepository, MoviesRepository moviesRepository) {
        this.movieRepository = movieRepository;
        this.moviesRepository = moviesRepository;
    }

    private static final String API_KEY = "cd04853d";
    String url = "http://www.omdbapi.com/";
    WebClient webClient = WebClient.create(url);
    WebClient posterAvailabilityCheckClient = WebClient.create();

    public OmdbSearchResults getMoviesByQuery(String query) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", API_KEY)
                        .queryParam("s", query)
                        .build())
                .retrieve()
                .onStatus(status -> status.value() >= HttpStatus.BAD_REQUEST.value(), clientResponse ->
                        Mono.error(new NetflixException((HttpStatus)clientResponse.statusCode(), "Request failed from getMovieDetails()")))
                .bodyToMono(new ParameterizedTypeReference<OmdbSearchResults>() {
                })
                .block();

    }


    private Mono<Throwable> handleErrors(ClientResponse response ) {
        return response.bodyToMono(String.class).flatMap(body -> {
            log.error("LOg errror");
            return Mono.error(new Exception());
        });
    }

    public Movie getMovieByTitle(Node node) {

        ImdbMovie res = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", API_KEY)
                        .queryParam("t", node.getMovieName())
                        .queryParam("y", node.getYear())
                        .build())
                .retrieve()
                .bodyToMono(ImdbMovie.class)
                .block();


        if(res.getTitle()==null) return Movie.builder().build();

            // save res to the movie table
            Movie movieEntity = movieRepository.save(Movie.builder()
                    .actors(res.getActors())
                    .awards(res.getAwards())
                    .boxOffice(res.getBoxOffice())
                    .country(res.getCountry())
                    .director(res.getDirector())
                    .dvd(res.getDvd())
                    .genre(res.getGenre())
                    .runtime(res.getRuntime())
                    .imdbId(res.getImdbId())
                    .imdbRating(res.getImdbRating())
                    .imdbVotes(res.getImdbVotes())
                    .language(res.getLanguage())
                    .metascore(res.getMetascore())
                    .plot(res.getPlot())
                    .poster(res.getPoster())
                    .production(res.getProduction())
                    .rated(res.getRated())
//                  .ratings(res.getRatings())  -- we are not saving ratings
                    .response(res.getResponse())
                    .type(res.getType())
                    .website(res.getWebsite())
                    .writer(res.getWriter())
                    .title(res.getTitle())
                    .released(res.getReleased())
                    .year(res.getYear())
                    .released(res.getReleased())
                    .extension(node.getType())
                    .resolution(node.getResolution())
                    // data from node movielink and srtLink
                    .link(node.getLink())
//                    .srtLink(node.getStrLink())

                    .build());


        return movieEntity;

    }


    public ImdbMovie getMovieById(@PathVariable String id) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", API_KEY)
                        .queryParam("i", id)
                        .build())
                .retrieve()
                .bodyToMono(ImdbMovie.class)
                .block();

    }

    public Movies saveToMoviesByTitle(Node node) {

        ImdbMovie res = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", API_KEY)
                        .queryParam("t", node.getMovieName())
                        .queryParam("y", node.getYear())
                        .build())
                .retrieve()
                .bodyToMono(ImdbMovie.class)
                .block();


        if(res.getTitle()==null) return Movies.builder().build();

        // save res to the movie table
        Movies movieEntity = moviesRepository.save(Movies.builder()
                .actors(res.getActors())
                .awards(res.getAwards())
                .boxOffice(res.getBoxOffice())
                .country(res.getCountry())
                .director(res.getDirector())
                .dvd(res.getDvd())
                .genre(res.getGenre())
                .runtime(res.getRuntime())
                .imdbId(res.getImdbId())
                .imdbRating(res.getImdbRating())
                .imdbVotes(res.getImdbVotes())
                .language(res.getLanguage())
                .metascore(res.getMetascore())
                .plot(res.getPlot())
                .poster(res.getPoster())
                .production(res.getProduction())
                .rated(res.getRated())
//                  .ratings(res.getRatings())  -- we are not saving ratings
                .response(res.getResponse())
                .type(res.getType())
                .website(res.getWebsite())
                .writer(res.getWriter())
                .title(res.getTitle())
                .released(res.getReleased())
                .year(res.getYear())
                .released(res.getReleased())
                .extension(node.getType())
                .resolution(node.getResolution())
                // data from node movielink and srtLink
                .link(node.getLink())
//                    .srtLink(node.getStrLink())

                .build());


        return movieEntity;

    }
}
