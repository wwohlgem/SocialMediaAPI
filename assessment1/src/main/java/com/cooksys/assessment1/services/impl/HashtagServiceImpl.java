package com.cooksys.assessment1.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.assessment1.entities.Hashtag;
import com.cooksys.assessment1.exceptions.NotFoundException;
import com.cooksys.assessment1.mappers.HashtagMapper;
import com.cooksys.assessment1.model.HashtagDto;
import com.cooksys.assessment1.repositories.HashtagRepository;
import com.cooksys.assessment1.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    
    private final HashtagMapper hashtagMapper;
    
    @Override
    public List<HashtagDto> getAllHashtags() {
    	return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
    }
    
    @Override
    public HashtagDto getTagByLabel(String label) {
    	Optional<Hashtag> queriedHashtag = hashtagRepository.findHashtagByLabel("#" + label);
    	if(queriedHashtag.isEmpty()) {
    		throw new NotFoundException("The requested Hashtag doesn't exist");
    	}
    	return hashtagMapper.entityToDto(queriedHashtag.get());
    }

}
