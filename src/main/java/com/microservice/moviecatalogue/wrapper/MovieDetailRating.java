package com.microservice.moviecatalogue.wrapper;

import lombok.Data;

@Data
public class MovieDetailRating {

	public MovieDetailRating(String movie, String movieDesc, Integer rating) {
		this.movie = movie;
		this.movieDesc = movieDesc;
		this.rating = rating;
	}

	public MovieDetailRating() {

	}

	private String movie;

	private String movieDesc;

	private Integer rating;

}