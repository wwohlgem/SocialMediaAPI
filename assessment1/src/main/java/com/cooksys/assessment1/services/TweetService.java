package com.cooksys.assessment1.services;

import java.util.List;

import com.cooksys.assessment1.model.CredentialsDto;
import com.cooksys.assessment1.model.TweetRequestDto;
import com.cooksys.assessment1.model.TweetResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto getTweetById(Long id);

	TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto);

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	void addLikeToTweet(Long id, CredentialsDto credentialsDto);

}
