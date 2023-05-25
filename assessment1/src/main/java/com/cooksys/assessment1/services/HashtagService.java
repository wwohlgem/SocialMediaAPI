package com.cooksys.assessment1.services;

import java.util.List;

import com.cooksys.assessment1.model.HashtagDto;

public interface HashtagService {

	List<HashtagDto> getAllHashtags();

	HashtagDto getTagByLabel(String label);
	
}
