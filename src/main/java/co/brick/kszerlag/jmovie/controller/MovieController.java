package co.brick.kszerlag.jmovie.controller;

import co.brick.kszerlag.jmovie.dto.MovieDto;
import co.brick.kszerlag.jmovie.entity.MovieEntity;
import co.brick.kszerlag.jmovie.service.MovieService;
import co.brick.kszerlag.jmovie.utils.MongoId;
import co.brick.kszerlag.jmovie.utils.MovieMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/movies")
@Validated
public class MovieController {

    @Value("${app.upload.folder}")
    private String storagePath;
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid MovieDto movieDto) throws Exception {
        MovieEntity movieEntity = movieService.createMovie(MovieMapper.getINSTANCE().toMovieEntity(movieDto));
        return ResponseEntity.created(new URI("/movies/" + movieEntity.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> findAll() {
        List<MovieEntity> movieEntityList = movieService.findAll();
        return ResponseEntity.ok(MovieMapper.getINSTANCE().toMovieDtoList(movieEntityList));
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> findMovieById(@PathVariable @MongoId String movieId) {
        MovieEntity movieEntity = movieService.findMovieById(movieId);
        return ResponseEntity.ok(MovieMapper.getINSTANCE().toMovieDto(movieEntity));
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<MovieDto>> findMovieByName(@RequestParam String name) {
        List<MovieEntity> movieEntities = movieService.findMovieByName(name);
        return ResponseEntity.ok(MovieMapper.getINSTANCE().toMovieDtoList(movieEntities));
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<MovieDto> replaceMovie(@PathVariable String movieId, @Valid MovieDto newMovieDto) {
        MovieEntity movieEntity = movieService.updateMovie(movieId, MovieMapper.getINSTANCE().toMovieEntity(newMovieDto));
        return ResponseEntity.ok(MovieMapper.getINSTANCE().toMovieDto(movieEntity));
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity removeMovie(@PathVariable @MongoId String movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{movieId}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity uploadMovieImage(@PathVariable @MongoId String movieId, @RequestParam(name = "image") MultipartFile multipartFile) {
        try {
            String fileName = movieId;
            movieService.updateImagePath(movieId, String.format("%s/%s", storagePath, fileName));
            Files.copy(multipartFile.getInputStream(), Paths.get(storagePath), StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

}

