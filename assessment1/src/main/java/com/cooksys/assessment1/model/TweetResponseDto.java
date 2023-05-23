package com.cooksys.assessment1.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.cooksys.assessment1.entities.Tweet;
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

	private TweetRequestDto inReplyTo;

	private TweetRequestDto repostedBy;
}
