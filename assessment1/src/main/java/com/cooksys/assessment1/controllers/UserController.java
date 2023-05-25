package com.cooksys.assessment1.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
