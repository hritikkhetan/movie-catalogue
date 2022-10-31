package com.microservice.moviecatalogue.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class UserRating {
	
	public UserRating() {

	}
	
	public UserRating(String userId, String movieId, Integer rating) {
		this.userId = userId;
		this.movieId = movieId;
		this.rating = rating;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	public String userId;
	
	public String movieId;
	
	public Integer rating;
	
}