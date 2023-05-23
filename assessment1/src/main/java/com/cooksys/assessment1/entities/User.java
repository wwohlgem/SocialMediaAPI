package com.cooksys.assessment1.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="user_table")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp joined;

	@Column(nullable = false)
	private boolean deleted = false;

	@OneToMany(mappedBy = "author")
	private List<Tweet> tweets;

	@Embedded
	Credentials credentials;

	@Embedded
	Profile profile;

	@ManyToMany
	@JoinTable(name = "followers_following",
		joinColumns = @JoinColumn(name = "follower_id"),
		inverseJoinColumns = @JoinColumn(name = "following_id"))
	private List<User> followers;
	
	@ManyToMany(mappedBy = "followers")
	private List<User> following;
	
	@ManyToMany
	@JoinTable(name = "user_likes",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	private List<User> likedTweets;
	
	@ManyToMany
	@JoinTable(name = "user_mentions",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	private List<Tweet> userMentions;

}