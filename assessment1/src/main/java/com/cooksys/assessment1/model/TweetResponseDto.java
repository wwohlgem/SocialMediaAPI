package com.cooksys.assessment1.model;

import java.time.LocalDateTime;

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

	private LocalDateTime posted;

	private boolean deleted;

	private Tweet inReplyTo;

	private Tweet repostedBy;

}
