package com.cooksys.assessment1.mappers;

import com.cooksys.assessment1.entities.Hashtag;
import com.cooksys.assessment1.entities.User;
import com.cooksys.assessment1.model.HashtagResponseDto;
import com.cooksys.assessment1.model.UserRequestDto;
import com.cooksys.assessment1.model.UserResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //entity / entites to dto
    UserResponseDto entityToDto(User user);
    User dtoToEntity(UserRequestDto userRequestDto);
    List<UserResponseDto> entityToDtos(List<User> users);
    List<User> dtosToEntities(List<UserRequestDto> userRequestDtos);

}
