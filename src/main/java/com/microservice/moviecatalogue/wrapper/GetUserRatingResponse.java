package com.microservice.moviecatalogue.wrapper;

import java.util.List;

import com.microservice.moviecatalogue.model.UserRating;

import lombok.Data;

@Data
public class GetUserRatingResponse {

	List<UserRating> userRatingList;
	
}
