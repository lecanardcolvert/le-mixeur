package net.lemixeur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LeMixeurApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeMixeurApplication.class, args);
	}

	@GetMapping("/")
	public String index() {
		return "Bienvenue !";
	}
}
