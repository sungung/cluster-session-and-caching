package com.sungung.appcaching;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sungung.appcaching.entity.City;
import com.sungung.appcaching.repository.CityRepository;

@SpringBootApplication
public class AppCachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppCachingApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(CityRepository repo){
		return (args) -> {
			repo.save(new City("BNE", "Brisbane", "QLD"));
			repo.save(new City("SYD", "Sydney", "NSW"));
			repo.save(new City("MEL", "Melbourne", "VIC"));
			repo.save(new City("ADL", "Adelaide", "SA"));
			repo.save(new City("GEE", "Geelong", "VIC"));
			repo.save(new City("BAL", "Ballarat", "VIC"));
		};
	}
	
}
