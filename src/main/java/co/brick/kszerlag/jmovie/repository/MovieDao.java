package co.brick.kszerlag.jmovie.repository;

import co.brick.kszerlag.jmovie.entity.MovieEntity;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
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

    MovieEntity findById(String movieId) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.find(eq("_id", new ObjectId(movieId))).first();
    }

    List<MovieEntity> findByName(String name) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.find(eq("name", name)).into(new ArrayList<>());
    }

    List<MovieEntity> findAll() {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.find().into(new ArrayList<>());
    }

    UpdateResult update(MovieEntity movieEntity) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.updateOne(eq("_id", movieEntity.getId()), combine(set("name", movieEntity.getName()), set("img", movieEntity.getImage())));
    }

    DeleteResult delete(String movieId) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.deleteOne(eq("_id", new ObjectId(movieId)));
    }

    MovieEntity findAndUpdateMovieImagePath(String movieId, String imagePath) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.findOneAndUpdate(eq("_id", new ObjectId(movieId)), set("image", imagePath));
    }

    public List<MovieEntity> findMovieContainsName(String name) {
        MongoCollection<MovieEntity> collection = mongoDatabase.getCollection("movies", MovieEntity.class);
        return collection.find(regex("name", Pattern.compile(name, Pattern.CASE_INSENSITIVE)))
                .into(new ArrayList<>());
    }
}
