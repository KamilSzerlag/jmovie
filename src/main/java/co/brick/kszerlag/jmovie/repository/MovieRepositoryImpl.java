package co.brick.kszerlag.jmovie.repository;

import co.brick.kszerlag.jmovie.entity.MovieEntity;
import co.brick.kszerlag.jmovie.fault.MovieAlreadyExistsException;
import co.brick.kszerlag.jmovie.utils.ErrorsMsgConst;
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
    public MovieEntity save(MovieEntity movieEntity) {
        if (isNew(movieEntity)) {
            return movieDao.insert(movieEntity);
        } else {
            return movieDao.update(movieEntity);
        }
    }

    @Override
    public void deleteMovie(String movieId) {
        movieDao.delete(movieId);
    }

    private boolean isNew(MovieEntity movieEntity) {
        if (movieEntity.getId() != null) {
            return findMovieById(movieEntity.getId().toString()).isPresent();
        }
        if (movieEntity.getName() != null) {
            for (MovieEntity movie : findMovieByName(movieEntity.getName())) {
                if (movie.equals(movieEntity)) {
                    throw new MovieAlreadyExistsException(ErrorsMsgConst.MOVIE_ALREADY_EXISTS);
                }
            }
        }
        return true;
    }

    @Override
    public MovieEntity updateMovieImagePath(String movieId, String imagePath) {
        return movieDao.updateMovieImagePath(movieId, imagePath);
    }
}
