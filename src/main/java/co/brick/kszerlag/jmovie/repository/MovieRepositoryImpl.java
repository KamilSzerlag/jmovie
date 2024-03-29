package co.brick.kszerlag.jmovie.repository;

import co.brick.kszerlag.jmovie.entity.MovieEntity;
import co.brick.kszerlag.jmovie.fault.MovieAlreadyExistsException;
import co.brick.kszerlag.jmovie.consts.ErrorMsgConst;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MovieRepositoryImpl implements MovieRepository {

    private MovieDao movieDao;

    public MovieRepositoryImpl(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    @Override
    public List<MovieEntity> findAll() {
        return movieDao.findAll();
    }

    @Override
    public Optional<MovieEntity> findMovieById(String id) {
        return Optional.ofNullable(movieDao.findById(id));
    }

    @Override
    public List<MovieEntity> findMovieByName(String name) {
        return movieDao.findByName(name);
    }

    @Override
    public Optional<MovieEntity> save(MovieEntity movieEntity) {
        if (isNew(movieEntity)) {
            return Optional.ofNullable(movieDao.insert(movieEntity));
        }
        return Optional.empty();
    }

    @Override
    public boolean update(String movieId, MovieEntity movieEntity) {
        return movieDao.update(movieId, movieEntity).wasAcknowledged();
    }

    @Override
    public void deleteMovie(String movieId) {
        movieDao.delete(movieId).wasAcknowledged();
    }

    private boolean isNew(MovieEntity movieEntity) {
        if (movieEntity.getId() != null) {
            return findMovieById(movieEntity.getId().toString()).isPresent();
        }
        if (movieEntity.getName() != null) {
            for (MovieEntity movie : findMovieByName(movieEntity.getName())) {
                if (movie.equals(movieEntity)) {
                    throw new MovieAlreadyExistsException(ErrorMsgConst.MOVIE_ALREADY_EXISTS);
                }
            }
        }
        return true;
    }

    @Override
    public Optional<MovieEntity> updateMovieImagePath(String movieId, String imagePath) {
        return Optional.ofNullable(movieDao.findAndUpdateMovieImagePath(movieId, imagePath));
    }

    @Override
    public List<MovieEntity> searchMovieByName(String name) {
        return movieDao.findMovieContainsName(name);
    }
}
