package com.cooksys.assessment1.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.cooksys.assessment1.model.CredentialsDto;
import com.cooksys.assessment1.model.ProfileDto;
import com.cooksys.assessment1.model.TweetResponseDto;
import com.cooksys.assessment1.model.UserRequestDto;
import com.cooksys.assessment1.model.UserResponseDto;
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

	private void validateByCredentials(CredentialsDto credentialsDto) {

		// get the optional user. If it is empty, throw Not Found Exception
		if (userRepository.findByCredentialsAndDeletedFalse(credentialsMapper.dtoToEntity(credentialsDto)).isEmpty()) {
			throw new NotFoundException("User not found");
		}
	}

	private void validateByUsername(String username) {

		// get the optional user. If it is empty, throw Not Found Exception
		if (userRepository.findByCredentials_UsernameAndDeletedFalse(username).isEmpty()) {
			throw new NotFoundException("User not found");
		}
	}

	private User getUserByUsername(String username) {
		Optional<User> optionalUser = userRepository.findByCredentials_UsernameAndDeletedFalse(username);
		if (optionalUser.isEmpty() || optionalUser.get().isDeleted()) {
			throw new NotFoundException("The specified user does not exist");
		}
		User user = optionalUser.get();
		return user;
	}

	private List<User> getAllFollowingNotDeleted(User user) {
		List<User> userFollows = new ArrayList<>();
		for (User follow : user.getFollowing()) {
			if (!follow.isDeleted()) {
				userFollows.add(follow);
			}
		}
		return userFollows;
	}

	private List<User> getAllFollowersNotDeleted(User user) {
		List<User> userFollows = new ArrayList<>();
		for (User follow : user.getFollowers()) {
			if (!follow.isDeleted()) {
				userFollows.add(follow);
			}
		}
		return userFollows;
	}

	@Override
	public List<TweetResponseDto> getUserTweets(String username) {

		List<Tweet> tweets = tweetRepository.findAllByAuthor_Credentials_UsernameAndDeletedFalse(username);

		return tweetMapper.entitiesToDtos(tweets);

	}

	@Override
	public UserResponseDto deleteUser(CredentialsDto credentialsDto, String username) {
		// if the user exists, mark as deleted and return the useResponseDto
		// if the user doesn't exist, throw new NotFoundException
		// maybe make a helper method for validation

		validateByCredentials(credentialsDto);

		User userToDelete = userRepository
				.findByCredentialsAndDeletedFalse(credentialsMapper.dtoToEntity(credentialsDto)).get();

		UserResponseDto userBeforeDeleting = null;

		if (userToDelete.getCredentials().getUsername().equals(username)) {

			// only want the user to be able to delete their own profile. So their username
			// must match the one they passed in the credentials
			userBeforeDeleting = userMapper.entityToDto(userToDelete);
			userToDelete.setDeleted(true);
			userRepository.saveAndFlush(userToDelete);

		} else
			throw new NotAuthorizedException("You cannot delete someone else's profile");
		return userBeforeDeleting;
	}

	@Override
	public UserResponseDto updateProfile(String username, UserRequestDto userRequestDto) {
		if(userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null ||
				userRequestDto.getCredentials().getUsername() == null ||
				userRequestDto.getCredentials().getPassword() == null) {
			throw new BadRequestException("Must provide user credentials and at least an email for the profile");
		}

		User userToUpdate = getUserByUsername(username);
		ProfileDto profileToSave = userRequestDto.getProfile();
		if(userRequestDto.getCredentials().getUsername() == userToUpdate.getCredentials().getUsername() &&
				userRequestDto.getCredentials().getPassword() == userToUpdate.getCredentials().getPassword()) {
			userToUpdate.setProfile(profileMapper.dtoToEntity(profileToSave));
		}
		return userMapper.entityToDto(userToUpdate);
	}

	@Override
	public UserResponseDto getUser(String username) {

		validateByUsername(username);

		User user = userRepository.findByCredentials_UsernameAndDeletedFalse(username).get();

		return userMapper.entityToDto(user);
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
		for (User user : userRepository.findAll()) {
			if (!user.isDeleted()) {
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
		for (User user : queriedUser.getFollowing()) {
			List<Tweet> userTweets = user.getTweets();
			for (Tweet tweet : userTweets) {
				if (!tweet.isDeleted()) {
					userFeed.add(tweet);
				}
			}
		}
		return tweetMapper.entitiesToDtos(userFeed);
	}

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		if (userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("You must include a username, password, and at least an email");
		}
		User userToSave = userMapper.dtoToEntity(userRequestDto);
		CredentialsDto credentials = userRequestDto.getCredentials();
		ProfileDto userProfile = userRequestDto.getProfile();

		if (credentials.getPassword() == null || credentials.getUsername() == null || userProfile.getEmail() == null) {
			throw new BadRequestException("Credentials must include username and password and at least an email");
		}
		String username = credentials.getUsername();
		Optional<User> optionalUser = userRepository.findByCredentials_UsernameAndDeletedFalse(username);
		if (optionalUser.isPresent()) {
			User existingUser = optionalUser.get();
			if (!existingUser.isDeleted()) {
				throw new BadRequestException("That username already exists, Please choose another username.");
			}
			existingUser.setDeleted(false);
			existingUser.setProfile(userToSave.getProfile());
			for (Tweet tweet : existingUser.getTweets()) {
				tweet.setDeleted(false);
			}
			userRepository.saveAndFlush(existingUser);
			tweetRepository.saveAllAndFlush(existingUser.getTweets());
			return userMapper.entityToDto(existingUser);
		} else {
			userToSave.setJoined(Timestamp.valueOf(LocalDateTime.now()));
			userRepository.saveAndFlush(userToSave);
			return userMapper.entityToDto(userToSave);
		}
	}

	@Override
	public void addFollow(String username, CredentialsDto credentialsDto) {
		if(credentialsDto.getPassword() == null) {
			throw new BadRequestException("The password is required");
		}
		User userToFollow = getUserByUsername(username);
		User lemming = getUserByUsername(credentialsDto.getUsername());
		if (userToFollow.getFollowers().contains(lemming)) {
			throw new BadRequestException("You are already following this user");
		}
		userToFollow.addFollower(lemming);
		lemming.addFollowing(userToFollow);
		userRepository.saveAndFlush(lemming);
		userRepository.saveAndFlush(userToFollow);
	}

	@Override
	public void removeFollow(String username, CredentialsDto credentialsDto) {
		if (credentialsDto.getPassword() == null) {
			throw new BadRequestException("You must include a username and password");
		}
		User userFollowing = getUserByUsername(username);
		User noLongerALemming = getUserByUsername(credentialsDto.getUsername());
		if (!userFollowing.getFollowers().contains(noLongerALemming)) {
			throw new BadRequestException("You are not currently following this user");
		}
		userFollowing.removeFollower(noLongerALemming);
		noLongerALemming.removeFollowing(userFollowing);
		userRepository.saveAndFlush(noLongerALemming);
		userRepository.saveAndFlush(userFollowing);
	}

}