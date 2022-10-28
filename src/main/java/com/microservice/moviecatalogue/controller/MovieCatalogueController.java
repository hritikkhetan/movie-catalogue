package com.microservice.moviecatalogue.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservice.moviecatalogue.dao.UserDaoRepository;
import com.microservice.moviecatalogue.model.Movie;
import com.microservice.moviecatalogue.model.User;
import com.microservice.moviecatalogue.model.UserRating;
import com.microservice.moviecatalogue.wrapper.CreateUserRequest;
import com.microservice.moviecatalogue.wrapper.GetUserRatingResponse;
import com.microservice.moviecatalogue.wrapper.MovieCatalogueRatingByUserResponse;
import com.microservice.moviecatalogue.wrapper.MovieDetailRating;

@RestController
public class MovieCatalogueController {
	
	@Autowired
	private UserDaoRepository userDaoRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping(value = "/user")
	public @ResponseBody User createUser(@RequestBody CreateUserRequest createUserRequest) {
				
		User user = new User();
		user.setUserId(createUserRequest.getUserId());
		user.setName(createUserRequest.getName());
		user.setEmailId(createUserRequest.getEmailId());
		userDaoRepository.save(user);
		
		return user;
	}
	
	@GetMapping(value = "/users")
	public @ResponseBody List<User> getUsers() {
		return userDaoRepository.findAll();
	}
	
	@GetMapping(value = "/user/{id}")
	public @ResponseBody User getUserById(@PathVariable Long id) {
		return userDaoRepository.findById(id).orElse(null);
	}
	
	@GetMapping(value = "/user/{userId}/movies")
	public @ResponseBody MovieCatalogueRatingByUserResponse getMovieCatalogueRatingByUser(@PathVariable String userId) {
		
		final User user = userDaoRepository.findByUserId(userId);
		
		MovieCatalogueRatingByUserResponse movieCatalogueRatingByUserResponse = new MovieCatalogueRatingByUserResponse();
		movieCatalogueRatingByUserResponse.setUserId(user.getUserId());
		movieCatalogueRatingByUserResponse.setName(user.getName());
		List<MovieDetailRating> movieDetailRatingList = new ArrayList<>();

		GetUserRatingResponse getUserRatingResponse= restTemplate.getForObject("http://rating-data-service/rating/" + user.getUserId(), GetUserRatingResponse.class);
		
		System.out.println(getUserRatingResponse);
		
		for(UserRating userRating : getUserRatingResponse.getUserRatingList()) {
			
			Movie movie = restTemplate.getForObject("http://movie-info-service/movie/" + userRating.getMovieId() , Movie.class);
			
			MovieDetailRating movieDetailRating = new MovieDetailRating();
			movieDetailRating.setMovie(movie.getMovieName());
			movieDetailRating.setMovieDesc(movie.getDescription());
			movieDetailRating.setRating(userRating.getRating());
			movieDetailRatingList.add(movieDetailRating);
		}
		
		movieCatalogueRatingByUserResponse.setMovieDetailRating(movieDetailRatingList);
		
		return movieCatalogueRatingByUserResponse;
	}
	
}
