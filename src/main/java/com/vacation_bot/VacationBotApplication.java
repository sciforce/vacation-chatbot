package com.vacation_bot;

import com.mongodb.MongoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories( basePackages = "com.vacation_bot.repositories" )

public class VacationBotApplication {

	public static void main( String[] args ) {
		SpringApplication.run(VacationBotApplication.class, args);
	}

	@Bean
	public MongoClient mongoClient() {
		return new MongoClient("localhost" );
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate( mongoClient(), "devbot" );
	}
}
