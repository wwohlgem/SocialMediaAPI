package com.cooksys.assessment1.mappers;

import org.mapstruct.Mapper;

import com.cooksys.assessment1.entities.Credentials;
import com.cooksys.assessment1.model.CredentialsDto;

@Mapper(componentModel="spring")
public interface CredentialsMapper {

	CredentialsDto entityToDto(Credentials credentials);
	
	Credentials dtoToEntity(CredentialsDto credentialsDto);
	
}
