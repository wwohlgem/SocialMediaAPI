package com.cooksys.assessment1.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.cooksys.assessment1.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TweetRequestDto {

	private User author;
	
	private Timestamp posted;
	
	private boolean deleted;
	
	private String content;
	
	private CredentialsDto credentialsDto;
	
}
