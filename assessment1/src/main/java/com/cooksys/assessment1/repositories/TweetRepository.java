package com.cooksys.assessment1.repositories;

import com.cooksys.assessment1.entities.Hashtag;
import com.cooksys.assessment1.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet,Long> {

//    Optional<Tweet> findByIdAndDeletedFalse();
    List<Hashtag> findAllByDeletedFalse();

    List<Tweet> findAllByAuthor_Credentials_UsernameAndDeletedFalse(String username);

    Optional<Tweet> findByIdAndDeletedFalse(Long id);


}
