package com.cooksys.assessment1.services.impl;

import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.exceptions.NotAuthorizedException;
import com.cooksys.assessment1.exceptions.NotFoundException;
import com.cooksys.assessment1.mappers.CredentialsMapper;
import com.cooksys.assessment1.mappers.ProfileMapper;
import com.cooksys.assessment1.mappers.TweetMapper;
import com.cooksys.assessment1.mappers.UserMapper;
import com.cooksys.assessment1.model.*;
import com.cooksys.assessment1.repositories.TweetRepository;
import com.cooksys.assessment1.repositories.UserRepository;
import com.cooksys.assessment1.services.UserService;
import com.cooksys.assessment1.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    private final CredentialsMapper credentialsMapper;

    private final ValidateServiceImpl validateServiceImpl;

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

        if(userToDelete.getCredentials().getUsername().equals(username)){

            //only want the user to be able to delete their own profile. So their username must match the one they passed in the credentials
            userToDelete.setDeleted(true);
            return userMapper.entityToDto(userRepository.saveAndFlush(userToDelete));

        }
        else throw new NotAuthorizedException("You cannot delete someone else's profile");
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
}
