package net.lemixeur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class LeMixeurApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeMixeurApplication.class, args);
	}

	@GetMapping("/")
	public String getIndex() {
		return "index";
	}
}
