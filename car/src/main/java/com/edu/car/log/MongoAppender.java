package com.edu.car.log;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 日志
 *
 * @author Administrator
 * @date 2019/1/21 17:26
 */
public class MongoAppender extends AppenderSkeleton {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<BasicDBObject> logsCollection;

    @Getter
    @Setter
    private String connectionUrl;

    @Getter
    @Setter
    private String databaseName;

    @Getter
    @Setter
    private String collectionName;

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if (mongoDatabase == null) {
            MongoClientURI connectionString = new MongoClientURI(connectionUrl);
            mongoClient = new MongoClient(connectionString);
            mongoDatabase = mongoClient.getDatabase(databaseName);
            logsCollection = mongoDatabase.getCollection(collectionName, BasicDBObject.class);
        }
        logsCollection.insertOne((BasicDBObject)loggingEvent.getMessage());
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}