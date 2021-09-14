package com.bookTracker.BookTracker;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import okhttp3.OkHttpClient;

@SpringBootApplication
public class BookTrackerApplication {

	@Bean
	public OkHttpClient client() {
		return new OkHttpClient();
	}

	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BookTrackerApplication.class, args);
	}

}
