package com.cooksys.assessment1.services;

import java.util.List;

import com.cooksys.assessment1.model.HashtagDto;
import com.cooksys.assessment1.model.TweetResponseDto;

public interface HashtagService {

	List<HashtagDto> getAllHashtags();

	List<TweetResponseDto> getTagsByLabel(String label);
	
}
