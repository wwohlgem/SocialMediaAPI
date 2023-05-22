package com.cooksys.assessment1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.model.TweetResponseDto;

@Mapper(componentModel = "spring")
public interface TweetMapper {

	TweetResponseDto entityToDto(Tweet tweet);

	List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);

}
