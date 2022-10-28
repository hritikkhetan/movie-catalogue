package com.microservice.moviecatalogue.wrapper;

import java.util.List;

import lombok.Data;

@Data
public class MovieCatalogueRatingByUserResponse {
	
	private String name;
	
	private String userId;
	
	private List<MovieDetailRating> movieDetailRating;
	
}
