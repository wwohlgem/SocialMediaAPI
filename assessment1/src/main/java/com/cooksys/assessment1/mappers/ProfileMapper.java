package com.cooksys.assessment1.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.assessment1.entities.Profile;
import com.cooksys.assessment1.model.ProfileDto;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

	Profile dtoToEntity(ProfileDto profileDto);
	ProfileDto entityToDto(Profile profile);
    List<Profile> dtosToEntities(List<ProfileDto> profileDtos);
    List<ProfileDto> entitiesToDtos(List<Profile> profiles);
	
}
