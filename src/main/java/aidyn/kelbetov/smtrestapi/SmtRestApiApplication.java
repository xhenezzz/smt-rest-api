package aidyn.kelbetov.smtrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class SmtRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmtRestApiApplication.class, args);
	}
 
}