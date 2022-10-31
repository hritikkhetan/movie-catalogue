package com.microservice.moviecatalogue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservice.moviecatalogue.model.Movie;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class MovieInfoService {

	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@HystrixCommand(fallbackMethod = "getFallbackMovieInfo")
	public Movie getMovieInfo(String movieId) {
		Movie movie = webClientBuilder.build().get()
				.uri("http://movie-info-service/movie/" + movieId).retrieve()
				.bodyToMono(Movie.class).block();

		// Movie movie = restTemplate.getForObject("http://movie-info-service/movie/" + userRating.getMovieId() , Movie.class);
		return movie;
	}
	
	public Movie getFallbackMovieInfo(String movieId) {
		return new Movie(movieId, "movie not available", "movie description not available");
	}
	
}
