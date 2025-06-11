package org.blogapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BlogApplicationWithAIIntegration {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplicationWithAIIntegration.class, args);
	}

}
