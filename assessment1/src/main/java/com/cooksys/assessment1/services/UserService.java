package com.cooksys.assessment1.services;

import java.util.List;

import com.cooksys.assessment1.model.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getUserFollowers(String username);

	List<UserResponseDto> getUserFollowing(String username);

	List<UserResponseDto> getAllUsers();

}
