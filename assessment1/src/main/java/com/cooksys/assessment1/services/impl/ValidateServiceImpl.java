package com.cooksys.assessment1.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.repositories.HashtagRepository;
import com.cooksys.assessment1.repositories.UserRepository;
import com.cooksys.assessment1.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final UserRepository userRepository;
    
    private final HashtagRepository hashtagRepository;

    @Override
    public boolean checkUsernameExists(String username) {

        //want to look through users not deleted
        //check if any of the usernames .equals(username)
        //if so, return true. False otherwise

    	Optional<User> notDeletedUser = userRepository.findByCredentials_UsernameAndDeletedFalse(username);
        return notDeletedUser.isPresent(); //if it's not empty, we found one, so true
    }
    
    @Override
    public boolean checkTagExists(String label) {
    	return hashtagRepository.findHashtagByLabel(label).isPresent();
    }
    
    @Override
    public boolean checkUsernameAvailable(String username) {
    	Optional<User> optionalUser = userRepository.findByCredentials_UsernameAndDeletedFalse(username);
    	return optionalUser.isEmpty();
    }
	//hello
}
