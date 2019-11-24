package co.brick.kszerlag.jmovie.repository;

import co.brick.kszerlag.jmovie.entity.MovieEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Component
public class MovieDao {

    private MongoDatabase mongoDatabase;

    public MovieDao(MongoDatabase mongoDb) {
        this.mongoDatabase = mongoDb;
    }

    MovieEntity insert(MovieEntity movieEntity) {
        ObjectId objectId = ObjectId.get();
        movieEntity.setId(objectId);
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        collection.insertOne(movieEntity);
        return movieEntity;
    }

    MovieEntity findById(String id) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.find(eq("id", id)).first();
    }

    List<MovieEntity> findByName(String name) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.find(eq("name", name)).into(new ArrayList<>());
    }

    List<MovieEntity> findAll() {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.find().into(new ArrayList<>());
    }

    MovieEntity update(MovieEntity movieEntity) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.findOneAndUpdate(eq("id", movieEntity.getId()), combine(set("name", movieEntity.getName()), set("img", movieEntity.getImage())));
    }

    void delete(String movieId) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        collection.deleteOne(eq("id", movieId));
    }

    MovieEntity updateMovieImagePath(String movieId, String imagePath) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        return collection.findOneAndUpdate(eq("id", movieId), set("image", imagePath));
    }
}
