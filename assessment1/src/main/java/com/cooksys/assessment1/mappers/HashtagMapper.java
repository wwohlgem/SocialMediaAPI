package com.cooksys.assessment1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.assessment1.entities.Hashtag;

import com.cooksys.assessment1.model.HashtagDto;

@Mapper(componentModel="spring")
public interface HashtagMapper {

	HashtagDto entityToDto(Hashtag hashtag);
	Hashtag dtoToEntity(HashtagDto hashtagDto);
	//dtoToEntity
	
	List<HashtagDto> entitiesToDtos(List<Hashtag> hashtags);
	List<Hashtag> dtosToEntities(List<HashtagDto> hashtagDtos);
	
}
