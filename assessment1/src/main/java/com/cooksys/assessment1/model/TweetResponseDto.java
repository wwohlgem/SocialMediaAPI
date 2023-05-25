package com.cooksys.assessment1.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.cooksys.assessment1.entities.Tweet;
import com.cooksys.assessment1.entities.User;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponseDto {

	private Long id;

	private UserResponseDto author;

	private Timestamp posted;

	private String content;

	private TweetResponseDto inReplyTo;
	private TweetResponseDto repostOf;
}
