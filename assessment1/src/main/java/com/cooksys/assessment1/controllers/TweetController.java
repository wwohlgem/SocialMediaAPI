package com.cooksys.assessment1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.assessment1.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tweets")
@RequiredArgsConstructor
public class TweetController {
	
	private final TweetService tweetService;

}
