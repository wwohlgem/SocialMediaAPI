package com.cooksys.assessment1.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.assessment1.model.CredentialsDto;
import com.cooksys.assessment1.model.TweetResponseDto;
import com.cooksys.assessment1.model.UserRequestDto;
import com.cooksys.assessment1.model.UserResponseDto;
import com.cooksys.assessment1.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	
    private final UserService userService;
    
    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getUserFollowers(@PathVariable String username) {
    	return userService.getUserFollowers(username);
    }
    
    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getUserFollowing(@PathVariable String username) {
    	return userService.getUserFollowing(username);
    }
    
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
    	return userService.getAllUsers();
    }
    
    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getAllUserMentions(@PathVariable String username) {
    	return userService.getAllUserMentions(username);
    }
    
    @GetMapping("/@{username}/feed")
    public List<TweetResponseDto> getUserFeed(@PathVariable String username) {
    	return userService.getUserFeed(username);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
    	return userService.createUser(userRequestDto);
    }
    
    @PostMapping("/@{username}/follow")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFollow(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
    	userService.addFollow(username, credentialsDto);
    }
    
    @PostMapping("/@{username}/unfollow")
    @ResponseStatus(HttpStatus.CREATED)
    public void removeFollow(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
    	userService.removeFollow(username, credentialsDto);
    }

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
