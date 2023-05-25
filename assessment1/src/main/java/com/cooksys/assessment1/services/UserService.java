package com.cooksys.assessment1.services;

import java.util.List;

import com.cooksys.assessment1.model.CredentialsDto;
import com.cooksys.assessment1.model.TweetResponseDto;
import com.cooksys.assessment1.model.UserRequestDto;
import com.cooksys.assessment1.model.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getUserFollowers(String username);

	List<UserResponseDto> getUserFollowing(String username);

	List<UserResponseDto> getAllUsers();

	List<TweetResponseDto> getAllUserMentions(String username);

	List<TweetResponseDto> getUserFeed(String username);

	UserResponseDto createUser(UserRequestDto userRequestDto);

	void addFollow(String username, CredentialsDto credentialsDto);

	void removeFollow(String username, CredentialsDto credentialsDto);
	
	List<TweetResponseDto> getUserTweets(String username);

    UserResponseDto deleteUser(CredentialsDto credentialsDto, String username);

    UserResponseDto updateProfile(String username, UserRequestDto userRequestDto);

    UserResponseDto getUser(String username);

}
