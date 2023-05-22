package com.cooksys.assessment1.repositories;

import com.cooksys.assessment1.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet,Long> {
	
	

}
