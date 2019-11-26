package co.brick.kszerlag.jmovie.service;

import co.brick.kszerlag.jmovie.entity.MovieEntity;
import co.brick.kszerlag.jmovie.fault.NoSuchMovieException;
import co.brick.kszerlag.jmovie.repository.MovieRepository;
import co.brick.kszerlag.jmovie.consts.ErrorMsgConst;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Optional<MovieEntity> createMovie(MovieEntity movieEntity) {
        return movieRepository.save(movieEntity);
    }

    @Override
    public MovieEntity findMovieById(String movieId) {
        return movieRepository.findMovieById(movieId)
                .orElseThrow(() -> new NoSuchMovieException(ErrorMsgConst.UPDATE_MOVIE_WITH_THIS_ID_NOT_EXISTS)
                );
    }

    @Override
    public List<MovieEntity> findMovieByName(String name) {
        return movieRepository.findMovieByName(name);
    }

    @Override
    public Optional<MovieEntity> updateMovie(String id, MovieEntity movieEntity) {
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
        return movieRepository.updateMovieImagePath(movieId, path)
                .orElseThrow(
                        () -> new NoSuchMovieException(ErrorMsgConst.IMAGE_UPDATE_MOVIE_WITH_THIS_ID_NOT_EXISTS)
                );
    }

    @Override
    public List<MovieEntity> searchMovieByName(String query) {
        return movieRepository.searchMovieByName(query);
    }
}
