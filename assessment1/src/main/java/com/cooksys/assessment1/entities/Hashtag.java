package com.cooksys.assessment1.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
public class Hashtag {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String label;

	@Column(nullable = false)
	private LocalDateTime firstUsed;

	@Column(nullable = false)
	private LocalDateTime lastUsed;

	@ManyToMany
	@JoinTable(
			name = "tweet_hashtags",
			joinColumns = @JoinColumn(name = "hashtag_id"),
			inverseJoinColumns = @JoinColumn(name = "tweet_id"))

	private Set<Tweet> hashtaggedTweets;

}
