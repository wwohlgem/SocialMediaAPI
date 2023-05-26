package com.cooksys.assessment1.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.Profile;
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

	private final ProfileMapper profileMapper;

	private User getByCredentials(CredentialsDto credentialsDto) {
		if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
			throw new NotAuthorizedException("Credentials are required");
		}
		Optional<User> optionalUser = userRepository
				.findByCredentialsUsernameAndDeletedFalse(credentialsDto.getUsername());
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User not found");
		}
		if (!optionalUser.get().getCredentials().getPassword().equals(credentialsDto.getPassword())) {
			throw new NotAuthorizedException("Password is invalid");
		}
		return optionalUser.get();
	}

	private User getUserByUsername(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		if (optionalUser.isEmpty() || optionalUser.get().isDeleted()) {
			throw new NotFoundException("The specified user does not exist");
		}
		return optionalUser.get();
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
		User user = getUserByUsername(username);
		return tweetMapper.entitiesToDtos(user.getTweets());

	}

	@Override
	public UserResponseDto deleteUser(CredentialsDto credentialsDto, String username) {
		User userToDelete = getByCredentials(credentialsDto);

		if (userToDelete.getCredentials().getUsername().equals(username)) {
			for (Tweet tweet : userToDelete.getTweets()) {
				tweet.setDeleted(true);
				tweetRepository.saveAndFlush(tweet);
			}
			userToDelete.setDeleted(true);
			userRepository.saveAndFlush(userToDelete);
		} else {
			throw new NotAuthorizedException("The provided credentials do not match the user to be deleted");
		}
		return userMapper.entityToDto(userToDelete);
	}

	@Override
	public UserResponseDto updateProfile(String username, UserRequestDto userRequestDto) {
		if (userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("Must provide user credentials and profile to update");
		}
		User userToUpdate = getByCredentials(userRequestDto.getCredentials());

		Profile profileToUpdate = profileMapper.dtoToEntity(userRequestDto.getProfile());
		if (profileToUpdate.getEmail() != null) {
			userToUpdate.getProfile().setEmail(profileToUpdate.getEmail());
		}
		if (profileToUpdate.getFirstName() != null) {
			userToUpdate.getProfile().setFirstName(profileToUpdate.getFirstName());
		}
		if (profileToUpdate.getLastName() != null) {
			userToUpdate.getProfile().setLastName(profileToUpdate.getLastName());
		}
		if (profileToUpdate.getPhone() != null) {
			userToUpdate.getProfile().setPhone(profileToUpdate.getPhone());
		}
		userRepository.saveAndFlush(userToUpdate);

		return userMapper.entityToDto(userToUpdate);
	}

	@Override
	public UserResponseDto getUser(String username) {
		return userMapper.entityToDto(getUserByUsername(username));
	}

	@Override
	public List<UserResponseDto> getUserFollowers(String username) {
		User queriedUser = getUserByUsername(username);
		Set<User> userFollowers = queriedUser.getFollowers();
		return userMapper
				.entityToDtos(userFollowers.stream().filter(user -> !user.isDeleted()).collect(Collectors.toList()));
	}

	@Override
	public List<UserResponseDto> getUserFollowing(String username) {
		User queriedUser = getUserByUsername(username);
		Set<User> userFollowers = queriedUser.getFollowing();
		return userMapper.entityToDtos(userFollowers.stream().filter(
				user -> !user.isDeleted()).collect(Collectors.toList()));
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

		if (userToSave.getCredentials().getPassword() == null || userToSave.getCredentials().getUsername() == null
				|| userToSave.getProfile().getEmail() == null) {
			throw new BadRequestException("Credentials must include username and password and at least an email");
		}
		Optional<User> optionalUser = userRepository
				.findByCredentialsUsername(userToSave.getCredentials().getUsername());
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
		if (credentialsDto.getPassword() == null) {
			throw new BadRequestException("The password is required");
		}
		User userToFollow = getUserByUsername(username);
		User lemming = getUserByUsername(credentialsDto.getUsername());
		if (userToFollow.getFollowers().contains(lemming)) {
			throw new BadRequestException("You are already following this user");
		}
		userToFollow.getFollowers().add(lemming);
		lemming.getFollowing().add(userToFollow);
		userRepository.saveAndFlush(userToFollow);
		userRepository.saveAndFlush(lemming);
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
		userFollowing.getFollowers().remove(noLongerALemming);
		noLongerALemming.getFollowing().remove(userFollowing);
		userRepository.saveAndFlush(userFollowing);
		userRepository.saveAndFlush(noLongerALemming);
	}

}
