package com.microservice.moviecatalogue.wrapper;

import java.util.List;

import lombok.Data;

@Data
public class MovieCatalogueByUserResponse {
	
	public MovieCatalogueByUserResponse(String userId, String name, List<MovieDetailRating> movieDetailRating) {
		this.userId = userId;
		this.name = name;
		this.movieDetailRating = movieDetailRating;
	}
	
	public MovieCatalogueByUserResponse() {
		
	}

	private String name;
	
	private String userId;
	
	private List<MovieDetailRating> movieDetailRating;
	
}
