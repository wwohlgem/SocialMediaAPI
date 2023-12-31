package com.cooksys.assessment1.controllers;


import com.cooksys.assessment1.entities.Hashtag;
import com.cooksys.assessment1.repositories.HashtagRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.assessment1.services.ValidateService;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {
	
	private final ValidateService validateService;
	private final HashtagRepository hashtagRepository;

	@GetMapping("/username/exists/@{username}")
	public boolean checkUsernameExists(@PathVariable String username) {
		return validateService.checkUsernameExists(username);
	}

	@GetMapping("/tag/exists/{label}")
	public boolean checkTagExists(@PathVariable String label) {
		return validateService.checkTagExists(label);
	}

	@GetMapping("/username/available/@{username}")
	public boolean checkUsernameAvailable(String username) {
		return validateService.checkUsernameAvailable(username);
	}

}
