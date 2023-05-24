package com.cooksys.assessment1.services;

import java.util.List;

import com.cooksys.assessment1.model.TweetResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto getTweetById(Long id);

}
