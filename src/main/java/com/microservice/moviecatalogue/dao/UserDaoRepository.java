package com.microservice.moviecatalogue.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.moviecatalogue.model.User;


@Repository
public interface UserDaoRepository extends JpaRepository<User, Long> {

	User findByUserId(String userId);
	
}
