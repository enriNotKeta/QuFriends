package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"app.repository"})
public class QuFriendsApplication {


	public static void main(String[] args) {
		SpringApplication.run(QuFriendsApplication.class, args);
	}
}
