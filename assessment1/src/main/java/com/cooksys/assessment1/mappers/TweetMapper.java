package com.cooksys.assessment1.mappers;

import java.util.List;

import com.cooksys.assessment1.model.TweetRequestDto;
import org.mapstruct.Mapper;

import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.model.TweetResponseDto;

@Mapper(componentModel = "spring", uses={UserMapper.class})
public interface TweetMapper {

	TweetResponseDto entityToDto(Tweet tweet);
	Tweet dtoToEntity(TweetRequestDto tweetRequestDto);

	List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);
	List<Tweet> dtosToEntities(List<TweetRequestDto> tweetRequestDtos);

}
