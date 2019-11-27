package co.brick.kszerlag.jmovie.controller;

import co.brick.kszerlag.jmovie.dto.MovieDto;
import co.brick.kszerlag.jmovie.entity.MovieEntity;
import co.brick.kszerlag.jmovie.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.brick.kszerlag.jmovie.consts.ErrorCodeConst.CONSTRAINT_VIOLATION;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MovieController.class)
@AutoConfigureMockMvc
class MovieControllerTest {

    private static final String MOVIE_JOKER_ID = "5ddc2fff26a6e33c4f6d6c20";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    private MovieEntity citizenJones;
    private MovieEntity citizen;
    private MovieEntity ironBridge;
    private MovieEntity hollywood;

    private MovieEntity joker;

    private MovieDto jokerDto;

    private List<MovieEntity> moviesFindAll;

    private List<MovieEntity> moviesSearchByName;

    private MovieDto entityWithImproperValues;

    @BeforeEach
    void setUp() {
        citizenJones = new MovieEntity("Obywatel Jones", "Anglik próboje wyjaśnic co się dzieje na ukrainie w latach 30-tych.");
        citizenJones.setId(new ObjectId("5ddc2fff26a6e33c4f6d6c20"));
        ironBridge = new MovieEntity("Żelazny most", "Przyjaźń podzialona przez kobietę i 30 metrów ziemi.");
        ironBridge.setId(new ObjectId("5ddc2fff26a6e33c4f6d6c2a"));
        hollywood = new MovieEntity("Pewnego razu w hollywood", "Aktor i jego kaskader starają się reanimować swoją karierę");
        hollywood.setId(new ObjectId("5ddc2fff26a6e33c4f6d6c2b"));

        joker = new MovieEntity("Joker", "Strudozny życiem komik popada w obłęd i staje się psychopatycznmy mordercą");
        joker.setId(new ObjectId(MOVIE_JOKER_ID));

        jokerDto = new MovieDto("Joker", "Strudozny życiem komik popada w obłęd i staje się psychopatycznmy mordercą");

        moviesFindAll = new ArrayList<>();
        moviesFindAll.add(citizenJones);
        moviesFindAll.add(ironBridge);
        moviesFindAll.add(hollywood);

        citizen = new MovieEntity("Obywatel", "Żywot poczciwego obywatale kraju nad wisłą.");
        citizen.setId(new ObjectId("5ddc2fff26a6e33c4f6d6c2c"));

        moviesSearchByName = new ArrayList<>();
        moviesSearchByName.add(citizenJones);
        moviesSearchByName.add(citizen);

        entityWithImproperValues = new MovieDto("Bad Movie", "");
    }

    @Test
    void create() throws Exception {
        given(movieService.createMovie(joker)).willReturn(Optional.of(joker));
        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jokerDto)))
                .andExpect(header().stringValues("Location", "/movies/" + MOVIE_JOKER_ID));
    }

    @Test
    void createBadRequest() throws Exception {
        given(movieService.createMovie(joker)).willReturn(Optional.of(joker));
        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entityWithImproperValues)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CONSTRAINT_VIOLATION));
    }

    @Test
    void findAll() throws Exception {
        given(movieService.findAll()).willReturn(moviesFindAll);
        mockMvc.perform(get("/movies", MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies.[0].id").value("5ddc2fff26a6e33c4f6d6c20"))
                .andExpect(jsonPath("$.movies.[1].id").value("5ddc2fff26a6e33c4f6d6c2a"))
                .andExpect(jsonPath("$.movies.[2].id").value("5ddc2fff26a6e33c4f6d6c2b"))
                .andExpect(jsonPath("$.movies.[0].name").value("Obywatel Jones"))
                .andExpect(jsonPath("$.movies.[1].name").value("Żelazny most"))
                .andExpect(jsonPath("$.movies.[2].name").value("Pewnego razu w hollywood"))
                .andExpect(jsonPath("$.movies.[0].description").value("Anglik próboje wyjaśnic co się dzieje na ukrainie w latach 30-tych."))
                .andExpect(jsonPath("$.movies.[1].description").value("Przyjaźń podzialona przez kobietę i 30 metrów ziemi."))
                .andExpect(jsonPath("$.movies.[2].description").value("Aktor i jego kaskader starają się reanimować swoją karierę"));

    }

    @Test
    void findMovieById() throws Exception {
        given(movieService.findMovieById("5ddc2fff26a6e33c4f6d6c20")).willReturn(joker);
        mockMvc.perform(get("/movies/{id}", MOVIE_JOKER_ID, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("5ddc2fff26a6e33c4f6d6c20"))
                .andExpect(jsonPath("$.name").value("Joker"))
                .andExpect(jsonPath("$.description").value("Strudozny życiem komik popada w obłęd i staje się psychopatycznmy mordercą"));
    }

    @Test
    void searchMovieByName() throws Exception {
        given(movieService.searchMovieByName("obywatel")).willReturn(moviesSearchByName);
        mockMvc.perform(get("/movies?filteredName={query}", "obywatel").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies.[0].id").value("5ddc2fff26a6e33c4f6d6c20"))
                .andExpect(jsonPath("$.movies.[1].id").value("5ddc2fff26a6e33c4f6d6c2c"))
                .andExpect(jsonPath("$.movies.[0].name").value("Obywatel Jones"))
                .andExpect(jsonPath("$.movies.[1].name").value("Obywatel"))
                .andExpect(jsonPath("$.movies.[0].description").value("Anglik próboje wyjaśnic co się dzieje na ukrainie w latach 30-tych."))
                .andExpect(jsonPath("$.movies.[1].description").value("Żywot poczciwego obywatale kraju nad wisłą."));
    }

    @Test
    void searchMovieByNameExpectedConstraintViolationException() throws Exception {
        given(movieService.searchMovieByName("obywatel")).willReturn(moviesSearchByName);
        mockMvc.perform(get("/movies?filteredName={query}", "1c0sRTVSWYaqNNTAq7s0BknpvnFbgS14QIuvPAHK").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CONSTRAINT_VIOLATION))
                .andExpect(jsonPath("$.message").value("searchMovieByName.filteredName: Query is to long."));
    }

    @Test
    void shouldReturnStatusNoContent() throws Exception {
        given(movieService.updateMovie(MOVIE_JOKER_ID, joker)).willReturn(true);
        mockMvc.perform(put("/movies/{movieId}", MOVIE_JOKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jokerDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatusNotFound() throws Exception {
        given(movieService.updateMovie(MOVIE_JOKER_ID, joker)).willReturn(false);
        mockMvc.perform(put("/movies/{movieId}", MOVIE_JOKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jokerDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeMovie() throws Exception {
        given(movieService.updateMovie(MOVIE_JOKER_ID, joker)).willReturn(false);
        mockMvc.perform(delete("/movies/{movieId}", MOVIE_JOKER_ID))
                .andExpect(status().isNoContent());
    }
}