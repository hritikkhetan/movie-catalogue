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
import com.microservice.moviecatalogue.wrapper.MovieCatalogueByUserResponse;
import com.microservice.moviecatalogue.wrapper.MovieDetailRating;

/**
 * MovieCatalogueController class to control and process the request by the user
 * @author hritik.khetan
 *
 */
@RestController
public class MovieCatalogueController {

	@Autowired
	private UserDaoRepository userDaoRepository;

	@Autowired
	private RatingDataService ratingDataService;

	@Autowired
	private MovieInfoService movieInfoService;

	/**
	 * createUser method is used to create the user of the movie-catalog application
	 * 
	 * @author hritik.khetan
	 * @param createUserRequest
	 * @return user: newly created user
	 */
	@PostMapping(value = "/user")
	public @ResponseBody User createUser(@RequestBody CreateUserRequest createUserRequest) {

		User user = new User();
		user.setUserId(createUserRequest.getUserId());
		user.setName(createUserRequest.getName());
		user.setEmailId(createUserRequest.getEmailId());
		userDaoRepository.save(user);

		return user;
	}

	/**
	 * getAllUsers method is used to fetch all the records of the user.
	 * 
	 * @author hritik.khetan
	 * @return list of all the users
	 */
	@GetMapping(value = "/users")
	public @ResponseBody List<User> getAllUsers() {
		return userDaoRepository.findAll();
	}

	/**
	 * getUserById method is used to fetch the record of the particular user by id.
	 * 
	 * @author hritik.khetan
	 * @param id: id of the user
	 * @return user by id
	 */
	@GetMapping(value = "/user/{id}")
	public @ResponseBody User getUserById(@PathVariable Long id) {
		return userDaoRepository.findById(id).orElse(null);
	}

	/**
	 * getMovieCatalogueByUser method is to fetch the movie information along with the rating for particular user.
	 * 
	 * @author hritik.khetan
	 * @param userId: unique userId of the user
	 * @return
	 */
	@GetMapping(value = "/user/{userId}/movies")
	public @ResponseBody MovieCatalogueByUserResponse getMovieCatalogueByUser(@PathVariable String userId) {

		final User user = userDaoRepository.findByUserId(userId);

		// Creating response object
		MovieCatalogueByUserResponse movieCatalogueByUserResponse = new MovieCatalogueByUserResponse();
		movieCatalogueByUserResponse.setUserId(user.getUserId());
		movieCatalogueByUserResponse.setName(user.getName());
		List<MovieDetailRating> movieDetailRatingList = new ArrayList<>();

		// Calling rating-data microservice to fetch the all the ratings the user has
		// given to the movies.
		GetUserRatingResponse getUserRatingResponse = ratingDataService.getUserRating(user.getUserId());

		for (UserRating userRating : getUserRatingResponse.getUserRatingList()) {

			// Calling movie-info ms to fetch the movie information
			Movie movie = movieInfoService.getMovieInfo(userRating.getMovieId());

			MovieDetailRating movieDetailRating = new MovieDetailRating(movie.getMovie(), movie.getDescription(),
					userRating.getRating());
			movieDetailRatingList.add(movieDetailRating);
		}

		movieCatalogueByUserResponse.setMovieDetailRating(movieDetailRatingList);

		return movieCatalogueByUserResponse;
	}

}