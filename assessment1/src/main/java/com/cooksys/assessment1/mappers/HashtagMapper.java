package com.cooksys.assessment1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.assessment1.entities.Hashtag;
import com.cooksys.assessment1.model.HashtagResponseDto;

@Mapper(componentModel="spring")
public interface HashtagMapper {

	HashtagResponseDto entityToDto(Hashtag hashtag);
	
	List<HashtagResponseDto> entitiesToDtos(List<Hashtag> hashtags);
	
}
