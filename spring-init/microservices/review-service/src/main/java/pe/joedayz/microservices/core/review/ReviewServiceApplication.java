package pe.joedayz.microservices.core.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("pe.joedayz")
public class ReviewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewServiceApplication.class, args);
	}

}
