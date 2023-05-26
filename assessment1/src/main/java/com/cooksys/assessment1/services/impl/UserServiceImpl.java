package com.cooksys.assessment1.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cooksys.assessment1.model.*;
import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.exceptions.BadRequestException;
import com.cooksys.assessment1.exceptions.NotAuthorizedException;
import com.cooksys.assessment1.exceptions.NotFoundException;
import com.cooksys.assessment1.mappers.CredentialsMapper;
import com.cooksys.assessment1.mappers.ProfileMapper;
import com.cooksys.assessment1.mappers.TweetMapper;
import com.cooksys.assessment1.mappers.UserMapper;
import com.cooksys.assessment1.repositories.TweetRepository;
import com.cooksys.assessment1.repositories.UserRepository;
import com.cooksys.assessment1.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    private final CredentialsMapper credentialsMapper;

//    private final ValidateServiceImpl validateServiceImpl;

    private final ProfileMapper profileMapper;


    private void validateByCredentials(CredentialsDto credentialsDto){

        //get the optional user. If it is empty, throw Not Found Exception
        if(userRepository.findByCredentialsAndDeletedFalse(credentialsMapper.dtoToEntity(credentialsDto)).isEmpty()){
            throw new NotFoundException("User not found");
        }
    }

    private void validateByUsername(String username){

        //get the optional user. If it is empty, throw Not Found Exception
        if(userRepository.findByCredentials_UsernameAndDeletedFalse(username).isEmpty()){
            throw new NotFoundException("User not found");
        }
    }


    @Override
    public List<TweetResponseDto> getUserTweets(String username) {

        List<Tweet> tweets = tweetRepository.findAllByAuthor_Credentials_UsernameAndDeletedFalse(username);
        return tweetMapper.entitiesToDtos(tweets);

    }

    @Override
    public UserResponseDto deleteUser(CredentialsDto credentialsDto, String username) {
        //if the user exists, mark as deleted and return the useResponseDto
        //if the user doesn't exist, throw new NotFoundException
        //maybe make a helper method for validation

        validateByCredentials(credentialsDto);

        User userToDelete = userRepository.findByCredentialsAndDeletedFalse(credentialsMapper.dtoToEntity(credentialsDto)).get();

		UserResponseDto userBeforeDeleting = null;

        if(userToDelete.getCredentials().getUsername().equals(username)){

            //only want the user to be able to delete their own profile. So their username must match the one they passed in the credentials
            userBeforeDeleting = userMapper.entityToDto(userToDelete);
			userToDelete.setDeleted(true);
            userRepository.saveAndFlush(userToDelete);

        }
        else throw new NotAuthorizedException("You cannot delete someone else's profile");
		return userBeforeDeleting;
    }

    @Override
    public UserResponseDto updateProfile(String username, UserRequestDto userRequestDto) {

        validateByUsername(username);

        User userToUpdate = userRepository.findByCredentialsAndDeletedFalse(credentialsMapper.dtoToEntity(userRequestDto.getCredentials())).get();

        userToUpdate.setProfile(profileMapper.dtoToEntity(userRequestDto.getProfile()));

        return userMapper.entityToDto(userRepository.saveAndFlush(userToUpdate));
    }

    @Override
    public UserResponseDto getUser(String username) {

        validateByUsername(username);

        User user = userRepository.findByCredentials_UsernameAndDeletedFalse(username).get();

        return userMapper.entityToDto(user);
    }	
    
    
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
		if(userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("You must include a username, password, and at least an email");
		}
		User userToSave = userMapper.dtoToEntity(userRequestDto);
		CredentialsDto credentials = userRequestDto.getCredentials();
		ProfileDto userProfile = userRequestDto.getProfile();

		if(credentials.getPassword() == null || credentials.getUsername() == null || userProfile.getEmail() == null) {
			throw new BadRequestException("Credentials must include username and password and at least an email");
		}
		String username = credentials.getUsername();
		Optional<User> optionalUser = userRepository.findByCredentials_UsernameAndDeletedFalse(username);
		if(optionalUser.isPresent()) {
			throw new BadRequestException("That username already exists, Please choose another username.");
		} else {
			userToSave.setCredentials(credentialsMapper.dtoToEntity(credentials));
			userToSave.setProfile(profileMapper.dtoToEntity(userProfile));

			userToSave.setJoined(Timestamp.valueOf(LocalDateTime.now()));
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
