package com.cooksys.assessment1.model;

import java.sql.Timestamp;

import com.cooksys.assessment1.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponseDto {

	private Long id;

	private User author;

	private Timestamp posted;

	private String content;

	private TweetResponseDto inReplyTo;

	private TweetResponseDto repostedBy;
}
