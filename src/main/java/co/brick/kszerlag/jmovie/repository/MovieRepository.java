package co.brick.kszerlag.jmovie.repository;

import co.brick.kszerlag.jmovie.entity.MovieEntity;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {

    List<MovieEntity> findAll();

    Optional<MovieEntity> findMovieById(String movieId);

    List<MovieEntity> findMovieByName(String name);

    MovieEntity save(MovieEntity movieEntity);

    void deleteMovie(String movieId);

    MovieEntity updateMovieImagePath(String movieId, String imagePath);

    List<MovieEntity> searchMovieByName(String searchedQuery);
}
