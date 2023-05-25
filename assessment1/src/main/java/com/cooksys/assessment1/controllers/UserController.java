package com.cooksys.assessment1.controllers;

import com.cooksys.assessment1.model.*;
import com.cooksys.assessment1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getUserTweets(@PathVariable String username){

        return userService.getUserTweets(username);

    }

    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@RequestBody CredentialsDto credentialsDto, @PathVariable String username){

        return userService.deleteUser(credentialsDto, username);

    }

    @PatchMapping("/@{username}")
    public UserResponseDto updateProfile(@PathVariable String username, @RequestBody UserRequestDto userRequestDto){
        return userService.updateProfile(username, userRequestDto);
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUser(@PathVariable String username){
        return userService.getUser(username);
    }




}
