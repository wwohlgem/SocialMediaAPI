package com.cooksys.assessment1.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.exceptions.BadRequestException;
import com.cooksys.assessment1.exceptions.NotFoundException;
import com.cooksys.assessment1.mappers.TweetMapper;
import com.cooksys.assessment1.model.CredentialsDto;
import com.cooksys.assessment1.model.TweetRequestDto;
import com.cooksys.assessment1.model.TweetResponseDto;
import com.cooksys.assessment1.repositories.TweetRepository;
import com.cooksys.assessment1.repositories.UserRepository;
import com.cooksys.assessment1.services.TweetService;
import com.cooksys.assessment1.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
	
	private final TweetMapper tweetMapper;
//	private final UserMapper userMapper;
	
	private final ValidateService validateService;
	
	private Tweet getTweet(Long id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndDeletedFalse(id);
		if(optionalTweet.isEmpty()) {
			throw new NotFoundException("Tweet could not be found");
		}
		return optionalTweet.get();
	}
	
	private User getUser(CredentialsDto credentialsDto) {
		Optional<User> optionalUser = userRepository.findByCredentials_UsernameAndDeletedFalse(credentialsDto.getUsername());
		if(optionalUser.isEmpty()) {
			throw new NotFoundException("The provided user does not exist");
		}
		return optionalUser.get();
	}
	
	@Override
	public List<TweetResponseDto> getAllTweets() {
		
		/*
		 * Retrieves all (non-deleted) tweets. The tweets 
		 * should appear in reverse-chronological order.
		 */
		
		return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalse());
	}
	
	@Override
	public TweetResponseDto getTweetById(Long id) {
		
		/*
		 * Retrieves a tweet with a given id. If no such tweet exists, 
		 * or the given tweet is deleted, an error should be sent in lieu of a response.
		 */
		
		Tweet queriedTweet = getTweet(id);
		return tweetMapper.entityToDto(queriedTweet);
	}
	
	@Override
	public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
		
		/*
		 * "Deletes" the tweet with the given id. If no such tweet exists or the provided credentials 
		 * do not match author of the tweet, an error should be sent in lieu of a response. If a tweet 
		 * is successfully "deleted", the response should contain the tweet data prior to deletion.

		   IMPORTANT: This action should not actually drop any records from the database! Instead, 
		   develop a way to keep track of "deleted" tweets so that even if a tweet is deleted, data 
		   with relationships to it (like replies and reposts) are still intact.
		 */
		
		
		
		Tweet queriedTweet = getTweet(id);
		
		if(validateService.checkUsernameExists(credentialsDto.getUsername())) {
			queriedTweet.setDeleted(true);			
			tweetRepository.saveAndFlush(queriedTweet);
			return tweetMapper.entityToDto(queriedTweet);
		} else {
			throw new BadRequestException("The specified user does not exist");
		}
	}
	
	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		
		/*
		 * Creates a new simple tweet, with the author set to the user identified by the credentials 
		 * in the request body. If the given credentials do not match an active user in the database, 
		 * an error should be sent in lieu of a response.

		   The response should contain the newly-created tweet.
		   Because this always creates a simple tweet, it must have a content property and may not 
		   have inReplyTo or repostOf properties.
		   IMPORTANT: when a tweet with content is created, the server must process the tweet's content 
		   for @{username} mentions and #{hashtag} tags. There is no way to create hashtags or create 
		   mentions from the API, so this must be handled automatically!
		   
		   
		   STILL NEED TO parse the strings for mentions and hashtags
		 */
		
		Tweet tweetToSave = tweetMapper.dtoToEntity(tweetRequestDto);
		CredentialsDto credentialsDto = tweetRequestDto.getCredentials();
		User tweetAuthor = getUser(tweetRequestDto.getCredentials());
		
		if(validateService.checkUsernameExists(credentialsDto.getUsername())) {
			userRepository.saveAndFlush(tweetAuthor);
			tweetToSave.setAuthor(tweetAuthor);
			tweetRepository.saveAndFlush(tweetToSave);
			return tweetMapper.entityToDto(tweetToSave);
		} else {
			throw new BadRequestException("The specified user does not exist");
		}
	}

}
