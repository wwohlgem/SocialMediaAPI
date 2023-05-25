package com.cooksys.assessment1.services.impl;

import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.mappers.UserMapper;
import com.cooksys.assessment1.repositories.UserRepository;
import com.cooksys.assessment1.services.UserService;
import com.cooksys.assessment1.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public boolean checkUsernameExists(String username) {

        //want to look through users not deleted
        //check if any of the usernames .equals(username)
        //if so, return true. False otherwise

        Optional<User> notDeletedUser = userRepository.findByCredentials_UsernameAndDeletedFalse(username);
        return notDeletedUser.isPresent(); //if it's not empty, we found one, so true
    }
}
