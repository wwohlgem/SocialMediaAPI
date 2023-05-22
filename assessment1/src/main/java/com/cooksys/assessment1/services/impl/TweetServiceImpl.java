package com.cooksys.assessment1.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.mappers.TweetMapper;
import com.cooksys.assessment1.repositories.TweetRepository;
import com.cooksys.assessment1.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	
	private final TweetRepository tweetRepository;
	
	private final TweetMapper tweetMapper;

}
