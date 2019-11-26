package co.brick.kszerlag.jmovie.repository;

import co.brick.kszerlag.jmovie.entity.MovieEntity;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MovieRepositoryTest {

    private static MongodExecutable mongodExecutable;
    private MovieRepository movieRepository;
    private MongoCollection<MovieEntity> movieEntityMongoCollection;

    @BeforeAll
    static void setUp() throws Exception {
        String ip = "localhost";
        int port = 27017;

        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.V4_0_2)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongodConfig);
        mongodExecutable.start();
    }

    @AfterAll
    static void stopMongoTestDatabase() {
        mongodExecutable.stop();
    }

    @BeforeEach
    void initializeFieldsOnStartUp() {
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase mongoDatabase = ((MongoDbFactory) new SimpleMongoClientDbFactory(MongoClients.create("mongodb://localhost"), "cobrick")).getDb().withCodecRegistry(codecRegistry);

        movieRepository = new MovieRepositoryImpl(new MovieDao(mongoDatabase));
        if (movieEntityMongoCollection != null) {
            movieEntityMongoCollection.drop();
        }
        movieEntityMongoCollection = mongoDatabase.getCollection("movies", MovieEntity.class);
    }

    @AfterEach
    void tearDown() {
        movieEntityMongoCollection.drop();
    }

    @Test
    void shouldReturnAllMovieEntitiesFromCollection() {
        MovieEntity christmasPrince = new MovieEntity("Świąteczny książę", "Zwykła dziewczyna poznaje w święta księcia.");
        christmasPrince.setId(new ObjectId());
        MovieEntity hollywood = new MovieEntity("Pewnego razu w hollywood", "Aktor i jego kaskader starają się reanimować swoją karierę");
        christmasPrince.setId(new ObjectId());
        MovieEntity ironBridge = new MovieEntity("Żelazny most", "Przyjaźń podzialona przez kobietę i 30 metrów ziemi.");
        christmasPrince.setId(new ObjectId());
        MovieEntity citizenJones = new MovieEntity("Obywatel Jones", "Anglik próboje wyjaśnic co się dzieje na ukrainie w latach 30-tych.");
        christmasPrince.setId(new ObjectId());

        List<MovieEntity> expectedList = new ArrayList<>();
        expectedList.add(christmasPrince);
        expectedList.add(hollywood);
        expectedList.add(ironBridge);
        expectedList.add(citizenJones);

        movieEntityMongoCollection.insertOne(christmasPrince);
        movieEntityMongoCollection.insertOne(hollywood);
        movieEntityMongoCollection.insertOne(ironBridge);
        movieEntityMongoCollection.insertOne(citizenJones);

        List<MovieEntity> all = movieRepository.findAll();

        assertEquals(4, all.size());
        assertEquals(expectedList, all);
    }

    @Test
    void shouldReturnMovieEntityByGivenMovieId() {
        String movieId = "5ddc2fff26a6e33c4f6d6c20";
        MovieEntity movieEntity = new MovieEntity("Joker", "Strudozny życiem komik popada w obłęd i staje się psychopatycznmy mordercą");
        movieEntity.setId(new ObjectId(movieId));
        Optional<MovieEntity> optionalMovieEntity = Optional.of(movieEntity);

        movieEntityMongoCollection.insertOne(movieEntity);

        assertEquals(optionalMovieEntity, movieRepository.findMovieById(movieId));
    }

    @Test
    void shoulReturnListOfMovieEnitiesByGivenName() {
        final String searchedName = "Obywatel Jones";
        MovieEntity citizenJones = new MovieEntity(searchedName, "Anglik próboje wyjaśnic co się dzieje na ukrainie w latach 30-tych.");
        MovieEntity citizen = new MovieEntity("Obywatel", "Żywot poczciwego obywatale kraju nad wisłą.");
        MovieEntity ironBridge = new MovieEntity("Żelazny most", "Przyjaźń podzialona przez kobietę i 30 metrów ziemi.");

        List<MovieEntity> movieEntities = new ArrayList<>();
        movieEntities.add(citizenJones);
        movieEntities.add(citizen);
        movieEntities.add(ironBridge);

        movieEntityMongoCollection.insertMany(movieEntities);

        List<MovieEntity> expectedList = new ArrayList<>();
        expectedList.add(citizenJones);
        assertEquals(expectedList, movieRepository.findMovieByName(searchedName));
    }

    @Test
    void shouldReturnListOfMovieEntitiesContainsQueryInName() {
        final String searchedQuery = "obywatel";
        MovieEntity citizenJones = new MovieEntity("Obywatel Jones", "Anglik próboje wyjaśnic co się dzieje na ukrainie w latach 30-tych.");
        MovieEntity citizen = new MovieEntity("Obywatel", "Żywot poczciwego obywatale kraju nad wisłą.");
        MovieEntity ironBridge = new MovieEntity("Żelazny most", "Przyjaźń podzialona przez kobietę i 30 metrów ziemi.");

        List<MovieEntity> movieEntities = new ArrayList<>();
        movieEntities.add(citizenJones);
        movieEntities.add(citizen);
        movieEntities.add(ironBridge);

        movieEntityMongoCollection.insertMany(movieEntities);

        List<MovieEntity> expectedList = new ArrayList<>();
        expectedList.add(citizenJones);
        expectedList.add(citizen);

        assertEquals(expectedList, movieRepository.searchMovieByName(searchedQuery));
    }

    @Test
    void shouldDeleteMovie() {
        MovieEntity movieEntity = new MovieEntity("Joker", "Strudozny życiem komik popada w obłęd i staje się psychopatycznmy mordercą");
        String movieId = "5ddc2fff26a6e33c4f6d6c20";
        movieEntity.setId(new ObjectId(movieId));
        movieEntityMongoCollection.insertOne(movieEntity);
        movieRepository.deleteMovie(movieId);
        assertTrue(movieRepository.findMovieById(movieId).isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "'Frozen II'     , 'Elsa i Anna wraz z przyjaciółmi udają się do "
                            + " Zaczarowanego Lasu w poszukiwaniu pomocy dla swego Królestwa.'",
            "'Obywatel Jones', 'Anglik próboje wyjaśnic co się dzieje na ukrainie w latach 30-tych.'",
            "'Obywatel'      , 'Żywot poczciwego obywatale kraju nad wisłą.'"
    })
    void shouldSaveMovieEntity(String name, String description) {
        MovieEntity movieEntity = new MovieEntity(name, description);
        Optional<MovieEntity> savedEntity = movieRepository.save(movieEntity);

        assertEquals(movieEntity, savedEntity.get());
        assertEquals(savedEntity.get(), movieRepository.findMovieByName(name).get(0));
    }


    @Test
    void updateMovieImagePath() {
        String movieId = "5ddc2fff26a6e33c4f6d6c20";
        MovieEntity movieEntity = new MovieEntity("Joker", "Strudozny życiem komik popada w obłęd i staje się psychopatycznmy mordercą");


    }
}