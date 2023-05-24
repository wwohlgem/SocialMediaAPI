package com.cooksys.assessment1.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.exceptions.NotFoundException;
import com.cooksys.assessment1.mappers.TweetMapper;
import com.cooksys.assessment1.model.TweetResponseDto;
import com.cooksys.assessment1.repositories.TweetRepository;
import com.cooksys.assessment1.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	
	private final TweetMapper tweetMapper;
	
	private Tweet getTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
		if(optionalTweet.isEmpty()) {
			throw new NotFoundException("Tweet could not be found");
		}
		return optionalTweet.get();
	}
	
	@Override
	public List<TweetResponseDto> getAllTweets() {
		/*
		 * needs to be in reverse chronological order
		 */
		
		return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalse());
	}
	
	@Override
	public TweetResponseDto getTweetById(Long id) {
		Tweet queriedTweet = getTweet(id);
		return tweetMapper.entityToDto(queriedTweet);
	}

}
