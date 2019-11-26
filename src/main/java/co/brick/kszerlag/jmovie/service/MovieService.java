package co.brick.kszerlag.jmovie.service;

import co.brick.kszerlag.jmovie.entity.MovieEntity;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Optional<MovieEntity> createMovie(MovieEntity movieEntity);

    MovieEntity findMovieById(String movieId);

    List<MovieEntity> findMovieByName(String name);

    Optional<MovieEntity> updateMovie(String movieId, MovieEntity movieEntity);

    MovieEntity updateImagePath(String movieId, String path);

    List<MovieEntity> findAll();

    void deleteMovie(String movieId);

    List<MovieEntity> searchMovieByName(String query);
}
