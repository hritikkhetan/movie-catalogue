package com.microservice.moviecatalogue.wrapper;

import lombok.Data;

@Data
public class CreateUserResponse {

	private String userId;
	
	private String name;
	
	private String emailId;
	
}
