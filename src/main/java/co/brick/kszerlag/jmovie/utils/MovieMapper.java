package co.brick.kszerlag.jmovie.utils;

import co.brick.kszerlag.jmovie.dto.MovieDto;
import co.brick.kszerlag.jmovie.entity.MovieEntity;

import java.util.List;
import java.util.stream.Collectors;

public class MovieMapper {

    private static MovieMapper INSTANCE;

    private MovieMapper() {
    }

    public static MovieMapper getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new MovieMapper();
        }
        return INSTANCE;
    }

    public MovieEntity toMovieEntity(MovieDto movieDto) {
        if (movieDto == null) {
            return null;
        }
        return new MovieEntity(
                movieDto.getName(),
                movieDto.getDescription(),
                movieDto.getImage()
        );
    }

    public MovieDto toMovieDto(MovieEntity movieEntity) {
        if (movieEntity == null) {
            return null;
        }
        return new MovieDto(
                movieEntity.getId() != null ? movieEntity.getId().toString() : null,
                movieEntity.getName(),
                movieEntity.getDescription(),
                movieEntity.getImage()
        );
    }

    public List<MovieDto> toMovieDtoList(List<MovieEntity> movieEntityList) {
        if (movieEntityList == null || movieEntityList.isEmpty()) {
            return null;
        }
        return movieEntityList.stream()
                .map(movieEntity -> toMovieDto(movieEntity))
                .collect(Collectors.toList());
    }
}
