package com.example.aravind_demo;

import java.util.function.Predicate;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableSwagger2WebFlux
public class Application implements CommandLineRunner {
	
	//@Autowired
	//private CustomerRepository repo;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		/*
		// Clear DB.
		this.repo.deleteAll();
		
		// Add test users.
		this.repo.save(new Customer("John", "Smith"));
		this.repo.save(new Customer("John", "Ruisi"));
		this.repo.save(new Customer("Alice", "Smith"));
		*/
	}
	
	// SWAGGER_UI - necessary methods
	@Bean
    public Docket publishedAPI() {
        return (new Docket(DocumentationType.SWAGGER_2)).groupName("Test Service").apiInfo(this.apiInfo()).select().paths(this.servicePaths()).build();
    }

    private Predicate<String> servicePaths() {
        return PathSelectors.regex("/.*");
    }

    private ApiInfo apiInfo() {
        return (new ApiInfoBuilder()).title("Customers REST API").description("REST API")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE").version("1.0").build();
    }

}
