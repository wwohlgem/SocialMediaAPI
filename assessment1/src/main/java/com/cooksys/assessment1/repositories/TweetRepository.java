package com.cooksys.assessment1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.assessment1.entities.Hashtag;
import com.cooksys.assessment1.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Hashtag> findAllByDeletedFalse();

    List<Tweet> findAllByAuthorCredentialsUsernameAndDeletedFalse(String username);

    Optional<Tweet> findByIdAndDeletedFalse(Long id);

}
