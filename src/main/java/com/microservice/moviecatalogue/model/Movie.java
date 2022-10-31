package com.microservice.moviecatalogue.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Movie {

	public Movie(String movieId, String movie, String description) {
		this.movieId = movieId;
		this.movie = movie;
		this.description = description;
	}
	
	public Movie() {
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String movieId;
	
	private String movie;
	
	private String description;
}