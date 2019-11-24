package co.brick.kszerlag.jmovie.controller;

import co.brick.kszerlag.jmovie.dto.MovieDto;
import co.brick.kszerlag.jmovie.service.MovieService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MovieControllerTest.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class MovieControllerTest {

    private MovieController movieController;

    MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    @BeforeAll
    void setUp() {
        movieController = new MovieController(movieService);
    }

    @Test
    void create_shouldReturnUriToCreatedResource() {
//        MovieDto movieDto = new MovieDto("Joker", "Sad man in sad world");
//        mockMvc.perform(post("/movies").accept()).andExpect()
    }

    @Test
    void findAll_shouldReturnJsonWithExpectedListOfObjects() {
    }

    @Test
    void findMovieById_shouldReturnJsonWithExpectedObject() {
    }

    @Test
    void findMovieByName_() {
    }

    @Test
    void replaceMovie() {
    }
}