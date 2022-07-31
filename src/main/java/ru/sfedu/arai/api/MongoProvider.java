package ru.sfedu.arai.api;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.SocketSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import ru.sfedu.arai.Constants;
import ru.sfedu.arai.utils.ConfigurationUtil;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoProvider {
    private static final Logger logger = LogManager.getLogger(MongoProvider.class);

    public enum RepositoryType {CSV, JDBC, XML}

    public enum COMMAND_TYPE {ADDED, DELETED, UPDATED}

    private static <T> MongoCollection<Document> getCollection(Class<T> clazz) throws IOException {
        ServerAddress serverAddress = new ServerAddress(
                ConfigurationUtil.getConfigurationEntry(Constants.MONGO_HOST),
                Integer.parseInt(ConfigurationUtil.getConfigurationEntry(Constants.MONGO_PORT)));

        MongoClientOptions.Builder optionsBuilder = new MongoClientOptions.Builder();

        optionsBuilder.serverSelectionTimeout(Integer.parseInt(ConfigurationUtil.getConfigurationEntry(Constants.MONGO_TIMEOUT)));
        optionsBuilder.socketTimeout(Integer.parseInt(ConfigurationUtil.getConfigurationEntry(Constants.MONGO_TIMEOUT)));
        optionsBuilder.connectTimeout(Integer.parseInt(ConfigurationUtil.getConfigurationEntry(Constants.MONGO_TIMEOUT)));
        optionsBuilder.writeConcern( WriteConcern.ACKNOWLEDGED );
        optionsBuilder.readPreference( ReadPreference.primary() );

        MongoClientOptions clientOptions = optionsBuilder.build();
        MongoClient mongoClient = new MongoClient(serverAddress, clientOptions);

        CodecRegistry codecRegistry = fromRegistries(
                MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoDatabase database = mongoClient.getDatabase(ConfigurationUtil.getConfigurationEntry(Constants.MONGO_DB)).withCodecRegistry(codecRegistry);
        return database.getCollection(clazz.getSimpleName().toLowerCase());
    }

    public static <T> void save(COMMAND_TYPE command, RepositoryType repositoryType, T obj) throws IOException {
        logger.info("save [1]: command = {}, type = {}, object = {}", command, repositoryType, obj);
        try {
            MongoCollection<Document> collection = getCollection(obj.getClass());

            Document document = new Document();
            document.put(Constants.MONGO_FIELD_TIME, new Date());
            document.put(Constants.MONGO_FIELD_COMMAND, command.toString());
            document.put(Constants.MONGO_FIELD_REPOSITORY, repositoryType.toString());
            document.put(Constants.MONGO_FIELD_OBJECT, obj);
            collection.insertOne(document);
        } catch (Exception e) {
            logger.error("save [2]: {}", e.getMessage());
        }
    }
}
