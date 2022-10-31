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

import com.microservice.moviecatalogue.dao.UserDaoRepository;
import com.microservice.moviecatalogue.model.Movie;
import com.microservice.moviecatalogue.model.User;
import com.microservice.moviecatalogue.model.UserRating;
import com.microservice.moviecatalogue.service.MovieInfoService;
import com.microservice.moviecatalogue.service.RatingDataService;
import com.microservice.moviecatalogue.wrapper.CreateUserRequest;
import com.microservice.moviecatalogue.wrapper.GetUserRatingResponse;
import com.microservice.moviecatalogue.wrapper.MovieCatalogueRatingByUserResponse;
import com.microservice.moviecatalogue.wrapper.MovieDetailRating;

@RestController
public class MovieCatalogueController {

	@Autowired
	private UserDaoRepository userDaoRepository;
	
	@Autowired
	private RatingDataService ratingDataService;
	
	@Autowired
	private MovieInfoService movieInfoService;

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

		// Creating response object
		MovieCatalogueRatingByUserResponse movieCatalogueRatingByUserResponse = new MovieCatalogueRatingByUserResponse();
		movieCatalogueRatingByUserResponse.setUserId(user.getUserId());
		movieCatalogueRatingByUserResponse.setName(user.getName());
		List<MovieDetailRating> movieDetailRatingList = new ArrayList<>();

		// Calling rating-data microservice to fetch the all the ratings the user has
		// given to the movies.
		GetUserRatingResponse getUserRatingResponse = ratingDataService.getUserRating(user.getUserId());


		for (UserRating userRating : getUserRatingResponse.getUserRatingList()) {

			// Calling movie-info ms to fetch the movie information
			Movie movie = movieInfoService.getMovieInfo(userRating.getMovieId());

			MovieDetailRating movieDetailRating = new MovieDetailRating(movie.getMovie(), movie.getDescription(), userRating.getRating());
			movieDetailRatingList.add(movieDetailRating);
		}

		movieCatalogueRatingByUserResponse.setMovieDetailRating(movieDetailRatingList);

		return movieCatalogueRatingByUserResponse;
	}

}