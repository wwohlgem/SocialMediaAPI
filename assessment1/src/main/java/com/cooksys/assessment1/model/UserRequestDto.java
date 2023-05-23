package com.cooksys.assessment1.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequestDto {
	
	private CredentialsDto credentials;
    private ProfileDto profile;

}
