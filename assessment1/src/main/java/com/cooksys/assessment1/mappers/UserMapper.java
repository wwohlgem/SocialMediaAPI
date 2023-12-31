package com.cooksys.assessment1.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.model.UserRequestDto;
import com.cooksys.assessment1.model.UserResponseDto;

@Mapper(componentModel = "spring", uses = {CredentialsMapper.class, ProfileMapper.class})
public interface UserMapper {

    
	@Mapping(target="username", source="credentials.username")
    UserResponseDto entityToDto(User entity);
	
    User dtoToEntity(UserRequestDto userRequestDto);
    
    List<UserResponseDto> entityToDtos(List<User> users);
    
    List<User> dtosToEntities(List<UserRequestDto> userRequestDtos);

}
