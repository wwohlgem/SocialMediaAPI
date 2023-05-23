package com.cooksys.assessment1.entities;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp posted;

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

	@ManyToMany(mappedBy = "likedTweets")
	private List<User> likes;
	
	@ManyToMany(mappedBy = "userMentions")
	private List<User> mentions;

}
