package com.microservice.moviecatalogue.wrapper;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class CreateUserRequest {

	@NonNull
	private String userId;
	
	@NonNull
	private String name;
	
	@Nullable
	private String emailId;
	
}