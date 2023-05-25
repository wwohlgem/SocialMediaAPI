package com.cooksys.assessment1.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.exceptions.NotFoundException;
import com.cooksys.assessment1.mappers.UserMapper;
import com.cooksys.assessment1.model.UserResponseDto;
import com.cooksys.assessment1.repositories.UserRepository;
import com.cooksys.assessment1.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
    private final UserRepository userRepository;
    
    private final UserMapper userMapper;
    
    private User getUserByUsername(String username) {
    	Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty() || optionalUser.get().isDeleted()) {
			throw new NotFoundException("The specified user does not exist");
		}
		User user = optionalUser.get();
		return user;
	}
    
    private List<User> getAllUsersNotDeleted(User user) {
    	List<User> userFollows = new ArrayList<>();
    	for(User follow : user.getFollowing()) {
    		if(!follow.isDeleted()) {
    			userFollows.add(follow);
    		}
    	}
    	return userFollows;
    }
    
    @Override
    public List<UserResponseDto> getUserFollowers(String username) {
    	User queriedUser = getUserByUsername(username);
    	List<User> userFollowers = getAllUsersNotDeleted(queriedUser);
    	return userMapper.entityToDtos(userFollowers);
    }
    
    @Override
    public List<UserResponseDto> getUserFollowing(String username) {
    	User queriedUser = getUserByUsername(username);
    	List<User> userFollowing = getAllUsersNotDeleted(queriedUser);
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
    
}
