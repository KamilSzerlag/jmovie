package co.brick.kszerlag.jmovie.service;

import co.brick.kszerlag.jmovie.entity.MovieEntity;

import java.util.List;

public interface MovieService {

    MovieEntity createMovie(MovieEntity movieEntity);

    MovieEntity findMovieById(String movieId);

    List<MovieEntity> findMovieByName(String name);

    MovieEntity updateMovie(String movieId, MovieEntity movieEntity);

    MovieEntity updateImagePath(String movieId, String path);

    List<MovieEntity> findAll();

    void deleteMovie(String movieId);

}
