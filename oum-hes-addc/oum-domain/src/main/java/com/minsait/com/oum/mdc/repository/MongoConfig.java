//package com.minsait.com.oum.mdc.repository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.bson.UuidRepresentation;
//import org.bson.codecs.DocumentCodec;
//import org.bson.codecs.UuidCodec;
//import org.bson.codecs.configuration.CodecRegistries;
//import org.bson.codecs.configuration.CodecRegistry;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClients;
//
//@Configuration
//@EnableAutoConfiguration
//public class MongoConfig extends AbstractMongoClientConfiguration {
//	@Value("${mongo.database}")
//	String database;
//	@Value("${mongo.address}")
//	String connectionString;
//
//	@Override
//	public com.mongodb.client.MongoClient mongoClient() {
//		List<CodecRegistry> codecRegistries = new ArrayList<>();
//		codecRegistries.add(CodecRegistries.fromCodecs(new UuidCodec(UuidRepresentation.STANDARD)));
//		codecRegistries.add(CodecRegistries.fromCodecs(new DocumentCodec()));
//		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(codecRegistries);
//
//		return MongoClients.create(MongoClientSettings.builder().codecRegistry(codecRegistry)
//				.applyConnectionString(new ConnectionString(connectionString)).build());
//	}
//
//	@Override
//	protected String getDatabaseName() {
//		// TODO Auto-generated method stub
//		return database;
//	}
//
//}