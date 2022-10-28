package com.microservice.moviecatalogue.wrapper;

import lombok.Data;

@Data
public class MovieDetailRating {

	private String movie;
	
	private String movieDesc;
	
	private float rating;
	
}