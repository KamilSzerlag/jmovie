package co.brick.kszerlag.jmovie.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.SslSettings;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class AppConfig {

    @Value("${app.mongodb_host}")
    private String host;

    @Bean
    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoClientDbFactory(MongoClients.create(host), "cobrick");
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDbFactory mongoDbFactory) {
        return new MongoTransactionManager(mongoDbFactory);
    }

    @Bean
    public MongoDatabase mongoDb() {
        return mongoDbFactory().getDb().withCodecRegistry(codecRegistry());
    }

    public MongoClientSettings clientSettings() {
        return MongoClientSettings.builder().applyToSslSettings(builder ->
                builder.applySettings(SslSettings.builder().enabled(false).build())).build();
    }

    private CodecRegistry codecRegistry() {
        return fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    }
}

