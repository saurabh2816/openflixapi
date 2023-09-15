package com.saurabh.netflixapi.repository;
import com.saurabh.netflixapi.entity.Movies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MoviesRepository extends JpaRepository<Movies, Long> {

    Optional<Movies> findByTitle(String title);

    List<Movies> findByResolution(String s);
}
