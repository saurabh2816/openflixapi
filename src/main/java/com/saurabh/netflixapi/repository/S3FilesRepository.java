package com.saurabh.netflixapi.repository;

import com.saurabh.netflixapi.entity.S3Files;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3FilesRepository extends JpaRepository<S3Files, Integer> {
}
