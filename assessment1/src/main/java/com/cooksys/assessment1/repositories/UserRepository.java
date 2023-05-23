package com.cooksys.assessment1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.assessment1.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	

}
