package com.wirvsvirus.homealonechallenge;

import com.wirvsvirus.homealonechallenge.db.SpringJdbc;
import kotlin.Unit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HomeAloneChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeAloneChallengeApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
			System.out.println(1);
			return String.format("Hello %s!", name);
	}
}
