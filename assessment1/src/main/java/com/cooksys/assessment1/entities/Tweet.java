package com.cooksys.assessment1.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	private String content;

	@ManyToOne
	private Tweet inReplyTo;

	@OneToMany(mappedBy = "inReplyTo")
	private List<Tweet> replies;

	@ManyToOne
	private Tweet repostOf;

	@OneToMany(mappedBy = "repostOf")
	private List<Tweet> reposts;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(
			name = "tweet_hashtags", 
			joinColumns = @JoinColumn(name = "hashtag_id"), 
			inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	private List<Hashtag> hashtags;

	@ManyToMany(mappedBy = "likedTweets")
	private List<User> likes;

	@ManyToMany
	@JoinTable(name = "user_mentions",
		joinColumns = @JoinColumn(name = "tweet_id"),
		inverseJoinColumns = @JoinColumn(name ="user_id"))
	private List<User> mentions;

}
