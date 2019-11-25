package co.brick.kszerlag.jmovie.service;

import co.brick.kszerlag.jmovie.entity.MovieEntity;
import co.brick.kszerlag.jmovie.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MovieServiceImpl implements MovieService {

    private MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public MovieEntity createMovie(MovieEntity movieEntity) {
        return movieRepository.save(movieEntity);
    }

    @Override
    public MovieEntity findMovieById(String movieId) {
        return movieRepository.findMovieById(movieId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<MovieEntity> findMovieByName(String name) {
        return movieRepository.findMovieByName(name);
    }

    @Override
    public MovieEntity updateMovie(String id, MovieEntity movieEntity) {
        return movieRepository.save(movieEntity);
    }

    @Override
    public List<MovieEntity> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public void deleteMovie(String movieId) {
        movieRepository.deleteMovie(movieId);
    }

    @Override
    public MovieEntity updateImagePath(String movieId, String path) {
        return movieRepository.updateMovieImagePath(movieId, path);
    }

    @Override
    public List<MovieEntity> searchMovieByName(String query) {
        return movieRepository.searchMovieByName(query);
    }
}
