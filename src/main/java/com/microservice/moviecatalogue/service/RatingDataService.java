package com.microservice.moviecatalogue.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.moviecatalogue.model.UserRating;
import com.microservice.moviecatalogue.wrapper.GetUserRatingResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class RatingDataService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getFallbackUserRating")
	public GetUserRatingResponse getUserRating(final String userId) {
		GetUserRatingResponse getUserRatingResponse = restTemplate
				.getForObject("http://rating-data-service/rating/" + userId, GetUserRatingResponse.class);
		return getUserRatingResponse;
	}
	
	public GetUserRatingResponse getFallbackUserRating(final String userId) {
		
		UserRating userRating = new UserRating(userId, "", 0);
		List<UserRating> userRatingList = new ArrayList<>();
		userRatingList.add(userRating);
		GetUserRatingResponse getUserRatingResponse = new GetUserRatingResponse();
		getUserRatingResponse.setUserRatingList(userRatingList);
		return getUserRatingResponse;
	}
	
}
