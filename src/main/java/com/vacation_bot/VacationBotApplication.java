package com.vacation_bot;

import com.mongodb.MongoClient;
import com.vacation_bot.core.classification.ClassificationService;
import com.vacation_bot.core.customization.CustomizationService;
import com.vacation_bot.repositories.DefaultRepositoryFactory;
import com.vacation_bot.repositories.RepositoryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@SpringBootApplication
@ImportResource( "classpath:META-INF.spring/application-context.xml" )
@EnableMongoRepositories( basePackages = "com.vacation_bot.repositories" )
public class VacationBotApplication {

	public static void main( String[] args ) {
		SpringApplication.run( VacationBotApplication.class, args );
	}

	@Bean
	public MongoClient mongoClient() {
		return new MongoClient("localhost" );
	}

	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate( mongoClient(), "devbot" );
	}

	@Bean
	DefaultRepositoryFactory repositoryFactory( List<MongoRepository> repositories ) {
		return new DefaultRepositoryFactory( repositories );
	}

	@Bean
	public CustomizationService customizationService( RepositoryFactory repositoryFactory ) {
		return new CustomizationService( repositoryFactory );
	}

	@Bean
	public ClassificationService classificationService() {
		return new ClassificationService();
	}
}
