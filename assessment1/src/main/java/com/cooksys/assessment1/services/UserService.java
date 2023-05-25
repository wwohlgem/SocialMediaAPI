package com.cooksys.assessment1.services;

import com.cooksys.assessment1.model.*;

import java.util.List;

public interface UserService {
    List<TweetResponseDto> getUserTweets(String username);

    UserResponseDto deleteUser(CredentialsDto credentialsDto, String username);

    UserResponseDto updateProfile(String username, UserRequestDto userRequestDto);

    UserResponseDto getUser(String username);
}
