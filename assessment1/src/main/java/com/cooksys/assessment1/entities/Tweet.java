package com.cooksys.assessment1.entities;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Tweet {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn
	private User author;

	@Column(nullable = false)
	private LocalDateTime posted;

	@Column(nullable = false)
	private boolean deleted = false;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn
	private Tweet inReplyTo;

	@ManyToOne
	@JoinColumn
	private Tweet repostOf;

	@ManyToMany(mappedBy = "hashtaggedTweets")
	private Set<Hashtag> hashtags;

}
