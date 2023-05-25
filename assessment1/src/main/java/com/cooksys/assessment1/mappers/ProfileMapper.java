package com.cooksys.assessment1.mappers;

import com.cooksys.assessment1.entities.Profile;
import com.cooksys.assessment1.model.ProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {



    Profile dtoToEntity(ProfileDto profileDto);


    ProfileDto entityToDto(Profile profile);

    List<Profile> dtosToEntities(List<ProfileDto> profileDtos);
    List<ProfileDto> entitiesToDtos(List<Profile> profiles);
}
