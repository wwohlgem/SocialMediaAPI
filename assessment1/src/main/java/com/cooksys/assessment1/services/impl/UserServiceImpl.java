package com.cooksys.assessment1.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.exceptions.BadRequestException;
import com.cooksys.assessment1.exceptions.NotFoundException;
import com.cooksys.assessment1.mappers.TweetMapper;
import com.cooksys.assessment1.mappers.UserMapper;
import com.cooksys.assessment1.model.CredentialsDto;
import com.cooksys.assessment1.model.TweetResponseDto;
import com.cooksys.assessment1.model.UserRequestDto;
import com.cooksys.assessment1.model.UserResponseDto;
import com.cooksys.assessment1.repositories.UserRepository;
import com.cooksys.assessment1.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
    private final UserRepository userRepository;
    
    private final UserMapper userMapper;
    private final TweetMapper tweetMapper;
    
    
    private User getUserByUsername(String username) {
    	Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty() || optionalUser.get().isDeleted()) {
			throw new NotFoundException("The specified user does not exist");
		}
		User user = optionalUser.get();
		return user;
	}
    
    private List<User> getAllFollowingNotDeleted(User user) {
    	List<User> userFollows = new ArrayList<>();
    	for(User follow : user.getFollowing()) {
    		if(!follow.isDeleted()) {
    			userFollows.add(follow);
    		}
    	}
    	return userFollows;
    }
    
    private List<User> getAllFollowersNotDeleted(User user) {
    	List<User> userFollows = new ArrayList<>();
    	for(User follow : user.getFollowers()) {
    		if(!follow.isDeleted()) {
    			userFollows.add(follow);
    		}
    	}
    	return userFollows;
    }
    
    @Override
    public List<UserResponseDto> getUserFollowers(String username) {
    	User queriedUser = getUserByUsername(username);
    	List<User> userFollowers = getAllFollowersNotDeleted(queriedUser);
    	return userMapper.entityToDtos(userFollowers);
    }
    
    @Override
    public List<UserResponseDto> getUserFollowing(String username) {
    	User queriedUser = getUserByUsername(username);
    	List<User> userFollowing = getAllFollowingNotDeleted(queriedUser);
    	return userMapper.entityToDtos(userFollowing);
    }
    
    @Override
    public List<UserResponseDto> getAllUsers() {
    	List<User> allUsers = new ArrayList<>();
    	for(User user : userRepository.findAll()) {
    		if(!user.isDeleted()) {
    			allUsers.add(user);
    		}
    	}
    	return userMapper.entityToDtos(allUsers);
    }
    
    @Override 
    public List<TweetResponseDto> getAllUserMentions(String username) {
    	User queriedUser = getUserByUsername(username);
    	List<Tweet> userMentions = queriedUser.getUserMentions();
    	return tweetMapper.entitiesToDtos(userMentions);
    }
    
    @Override
    public List<TweetResponseDto> getUserFeed(String username) {
    	User queriedUser = getUserByUsername(username);
    	List<Tweet> userFeed = queriedUser.getTweets();
    	for(User user : queriedUser.getFollowing()) {
    		List<Tweet> userTweets = user.getTweets();
    		for(Tweet tweet : userTweets) {
    			if(!tweet.isDeleted()) {
    				userFeed.add(tweet);
    			}
    		}
    	}
    	return tweetMapper.entitiesToDtos(userFeed); 
    }
    
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
    	User userToSave = userMapper.dtoToEntity(userRequestDto);
    	CredentialsDto credentials = userRequestDto.getCredentials();
    	String username = credentials.getUsername();
    	Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
    	if(optionalUser.isPresent()) {
    		throw new BadRequestException("That username already exists, Please choose another username.");
    	} else {
    		userRepository.saveAndFlush(userToSave);
    		return userMapper.entityToDto(userToSave);
    	}
    }
    
    @Override
    public void addFollow(String username, CredentialsDto credentialsDto) {
    	User userToFollow = getUserByUsername(username);
    	User lemming = getUserByUsername(credentialsDto.getUsername());
    	if(userToFollow.getFollowers().contains(lemming)) {
    		throw new BadRequestException("You are already following this user");
    	} else {
    		userToFollow.addFollower(lemming);
    		lemming.addFollowing(userToFollow);
    		userRepository.saveAndFlush(lemming);
    		userRepository.saveAndFlush(userToFollow);
    	}
    }
    
    @Override
    public void removeFollow(String username, CredentialsDto credentialsDto) {
    	User userFollowing = getUserByUsername(username);
    	User noLongerALemming = getUserByUsername(credentialsDto.getUsername());
    	if(userFollowing.getFollowers().contains(noLongerALemming)) {
    		userFollowing.removeFollower(noLongerALemming);
    		noLongerALemming.removeFollowing(userFollowing);
    		userRepository.saveAndFlush(noLongerALemming);
    		userRepository.saveAndFlush(userFollowing);
    	} else {
    		throw new BadRequestException("You are not currently following this user");
    	}
    }
    
}
