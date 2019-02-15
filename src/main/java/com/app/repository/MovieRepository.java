package com.app.repository;

import com.app.model.Movie;
import com.app.repository.generic.GenericRepository;

import java.util.Optional;

public interface MovieRepository extends GenericRepository<Movie> {
    Optional<Movie> findByTitle(String title);
}
