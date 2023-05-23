package com.cooksys.assessment1.model;

import java.time.LocalDateTime;
import java.util.List;

import com.cooksys.assessment1.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TweetRequestDto {

	private User author;
	
	private LocalDateTime posted;
	
	private boolean deleted;
	
	private String content;
	
	private List<CredentialsDto> credentials;
	
}
