package co.brick.kszerlag.jmovie.controller;

import co.brick.kszerlag.jmovie.dto.MovieDto;
import co.brick.kszerlag.jmovie.dto.ResponseError;
import co.brick.kszerlag.jmovie.entity.MovieEntity;
import co.brick.kszerlag.jmovie.fault.MovieAlreadyExistsException;
import co.brick.kszerlag.jmovie.fault.NoSuchMovieException;
import co.brick.kszerlag.jmovie.service.MovieService;
import co.brick.kszerlag.jmovie.consts.ErrorCodeConst;
import co.brick.kszerlag.jmovie.consts.ErrorMsgConst;
import co.brick.kszerlag.jmovie.utils.MongoId;
import co.brick.kszerlag.jmovie.utils.MovieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@Validated
public class MovieController {

    protected static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Value("${application.upload.folder}")
    private String storagePath;

    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid MovieDto movieDto) throws ConstraintViolationException {
        return movieService.createMovie(MovieMapper.getINSTANCE().toMovieEntity(movieDto))
                .map(movieEntity -> {
                    try {
                        return ResponseEntity.created(new URI("/movies/" + movieEntity.getId())).build();
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ResponseError(ErrorCodeConst.INVALID_URI, ErrorMsgConst.INVALID_URI));
                    }
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<Map<String, List<MovieDto>>> findAll() {
        List<MovieEntity> movieEntities = movieService.findAll();
        Map<String, List<MovieDto>> movies = new HashMap<>();
        movies.put("movies", MovieMapper.getINSTANCE().toMovieDtoList(movieEntities));
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> findMovieById(@PathVariable @MongoId String movieId) {
        MovieEntity movieEntity = movieService.findMovieById(movieId);
        return ResponseEntity.ok(MovieMapper.getINSTANCE().toMovieDto(movieEntity));
    }

    @GetMapping(params = "name")
    public ResponseEntity<Map<String,List<MovieDto>>> findMovieByName(@RequestParam String name) {
        List<MovieEntity> movieEntities = movieService.findMovieByName(name);
        Map<String, List<MovieDto>> moviesFound = new HashMap<>();
        moviesFound.put("movies", MovieMapper.getINSTANCE().toMovieDtoList(movieEntities));
        return ResponseEntity.ok(moviesFound);
    }

    @GetMapping(params = "filteredName")
    public ResponseEntity<Map<String,List<MovieDto>>> searchMovieByName(@RequestParam(defaultValue = "", name = "filteredName")
                                                                            @Size(max = 30, message = "Query is to long.")
                                                                                    String filteredName) throws ConstraintViolationException {
        Map<String, List<MovieDto>> filteredMovies = new HashMap<>();
        if (filteredName.isBlank()) {
            filteredMovies.put("movies", MovieMapper.getINSTANCE().toMovieDtoList(movieService.findAll()));
            return ResponseEntity.ok(filteredMovies);
        }
        List<MovieEntity> movieEntities = movieService.searchMovieByName(filteredName);
        filteredMovies.put("movies", MovieMapper.getINSTANCE().toMovieDtoList(movieEntities));
        return ResponseEntity.ok(filteredMovies);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity updateMovie(@PathVariable @MongoId String movieId, @RequestBody @Valid MovieDto newMovieDto) throws ConstraintViolationException {
        return movieService.updateMovie(movieId, MovieMapper.getINSTANCE().toMovieEntity(newMovieDto)) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{movieId}")
    public ResponseEntity removeMovie(@PathVariable @MongoId String movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{movieId}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity uploadMovieImage(@PathVariable @MongoId String movieId, @RequestParam(name = "image") MultipartFile multipartFile, HttpServletRequest request) {
        try {
            String fileName = movieId;
            MovieEntity movieEntity = movieService.updateImagePath(movieId, UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(request)).build().toUriString());
            Files.copy(multipartFile.getInputStream(), Paths.get(storagePath + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok(MovieMapper.getINSTANCE().toMovieDto(movieEntity));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseError(ErrorCodeConst.INTERNAL_ERROR, ErrorMsgConst.UNEXPECTED_ERROR));
        }
    }

    @GetMapping(value = "/{movieId}/image", produces = {MediaType.IMAGE_PNG_VALUE})
    private ResponseEntity<byte[]> downloadMovieImage(@PathVariable @MongoId String movieId) {
        String fileName = movieId;
        String filePath = storagePath + "/" + fileName;
        try {
            return new ResponseEntity<>(Files.readAllBytes(Paths.get(filePath)), HttpStatus.ACCEPTED);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ResponseError> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(new ResponseError(ErrorCodeConst.INTERNAL_ERROR, ErrorMsgConst.UNEXPECTED_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchMovieException.class)
    ResponseEntity<ResponseError> handleNoSuchMovieException(NoSuchMovieException e) {
        return new ResponseEntity<ResponseError>(new ResponseError(ErrorCodeConst.RESOURCE_NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MovieAlreadyExistsException.class)
    ResponseEntity<ResponseError> handleMovieAlreadyExistsException(MovieAlreadyExistsException e) {
        return new ResponseEntity<>(new ResponseError(ErrorCodeConst.RESOURCE_ALREADY_EXISTS, e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    ResponseEntity<ResponseError> handleConstraintViolationException(Exception e) {
        return new ResponseEntity<>(new ResponseError(ErrorCodeConst.CONSTRAINT_VIOLATION, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

